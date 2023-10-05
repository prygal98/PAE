import {clearActive, setActiveLink} from '../../utils/activeLink';
import {clearPage} from '../../utils/render';
import 'bootstrap/dist/css/bootstrap.min.css';
import {login} from '../../Domain/UserLibrary';
import Navbar from '../Navbar/Navbar';
import Navigate from "../Router/Navigate";
import {
  getAuthenticatedUser,
  setRememberMe
} from "../../utils/auths";
import {signup} from "../../utils/dictionary";

const formLogin = 
  `<div id="message"></div>
  <div class="row d-flex justify-content-center align-items-center h-100">
    <div class="col-xl-10">
      <div class="card rounded-3 text-black" id="backseparator">
        <div class="row g-0">
          <div class="">
            <div class="p-md-5 mx-md-4">
              
              <div class="text-center">
                <h1>Vous êtes de retour!</h1>                                        
              </div>

              <div class="d-flex align-items-center justify-content-center pb-4">
                <p class="mb-0 me-2">Vous n'avez pas de compte?</p>
                <button type="button" class="btn btn-outline-primary rounded-pill" id="register">${signup}</button>
              </div>
                         
              <div class="form-outline mb-4">
                <label class="form-label" for="form2Example11">E-mail</label>
                <input type="email" id="email" class="form-control" placeholder="Entrez votre adresse e-mail..." />
              </div>

              <div class="form-outline mb-4">
                <label class="form-label" for="form2Example22">Mot de passe</label>
                <input type="password" id="pwd" class="form-control" placeholder="Entrez votre mot de passe..." />
              </div>

              <div class="form-outline mb-4">
                <input type="checkbox" class="form-check-input" id="rememberme"> 
                <label for="rememberMe">Se souvenir de moi</label>
              </div>

              <div class="text-center pt-1 mb-5 pb-1">
                <button class="btn btn-success rounded-pill" id="login" type="button" style="color : white">S'identifier</button>
              </div>

            </div>
          </div>                         
        </div>
      </div>
    </div>
  </div>           
`;

const LoginPage = () => {
  // USER DEJA LOG
  const user = getAuthenticatedUser();
  if (user !== undefined) {
    Navigate('/profile');
  }

  // USER PAS LOG
  clearPage();
  setActiveLink('loginPage');
  Navbar();
  const main = document.querySelector('main');
  main.innerHTML = formLogin;

  actionRegisterPage();
  actionRememberMe();
  login();
};

function actionRegisterPage() {
  const btn = document.getElementById('register');

  btn.addEventListener('click', async (e) => {
    e.preventDefault();
    clearActive();
    Navigate('/register');
  });
}

function actionRememberMe() {
  const rememberME = document.getElementById('rememberme');

  rememberME.addEventListener('click', async (evt) => {
    setRememberMe(evt.target.checked);
  });
}

export default LoginPage;