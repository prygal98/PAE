import {removePathPrefix, usePathPrefix} from '../../utils/path-prefix';
import routes from './routes';
import Navbar from "../Navbar/Navbar";
import Navigate from "./Navigate";
import {
  clearAuthenticatedUser,
  isAuthenticated
} from "../../utils/auths";
import Footer from "../Footer/Footer";

const Router = () => {
  onFrontendLoad();
  onNavBarClick();
  onHistoryChange();
};

let token ;

function logout() {
  clearAuthenticatedUser();
  token = null;
  Navbar();
  Footer();
  Navigate('/login');
}

function onNavBarClick() {
  const navbarWrapper = document.querySelector('#navbarWrapper');

  navbarWrapper.addEventListener('click', (e) => {
    e.preventDefault();
    const navBarItemClicked = e.target;
    const uri = navBarItemClicked?.dataset?.uri;
    if (uri) {
      const componentToRender = routes[uri];
      if (!componentToRender) {
        throw Error(
            `The ${uri} ressource does not exist.`);
      }

      if (componentToRender === '/logout') {
        logout();
      } else {
        componentToRender();
        window.history.pushState({}, '', usePathPrefix(uri));
      }
    }
  });
}

function onHistoryChange() {
  window.addEventListener('popstate', () => {
    const uri = removePathPrefix(window.location.pathname);
    const componentToRender = routes[uri];
    componentToRender();
  });
}

token = localStorage.getItem('token');
let tokenIsIntoLocalStorage = 1;
if (token == null) {
  token = sessionStorage.getItem('token');
  tokenIsIntoLocalStorage = 0;
}
token = JSON.parse(token);

function onFrontendLoad() {
  window.addEventListener('load', () => {

    if (isAuthenticated() && token != null) {
      fetch('http://localhost:8080/users/me', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
          // TODO verif si bearer est utile ( envoyé dans
          // AuthorizationRequestFilter) est coupé par notre substring
          // actuellement
        }
      })
          .then(async response => {
            if (!response.ok) {
              throw new Error('problem with the reponse ');
            }
            let respons = await response.json();
            respons ={
              user: respons
            }
            if (tokenIsIntoLocalStorage === 1) {
              localStorage.setItem('user',JSON.stringify(respons.user));
            }
            if (tokenIsIntoLocalStorage === 0) {
              sessionStorage.setItem('user',JSON.stringify(respons.user));
            }
            return respons;
          })
          .then(data => {
            // eslint-disable no-console
            console.log(data);
          })
          .catch(error => {
            console.error('There was a problem with the network:', error);
          });
    }

    const uri = removePathPrefix(window.location.pathname);
    const componentToRender = routes[uri];
    if (!componentToRender) {
      throw Error(`The ${uri} ressource does not exist.`);
    }

    componentToRender();
  });
}

export default Router;
