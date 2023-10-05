// eslint-disable-next-line no-unused-vars
// import { Navbar as BootstrapNavbar } from 'bootstrap';
import {getAuthenticatedUser} from '../../utils/auths';
// eslint-disable-next-line import/no-cycle
import Navigate from '../Router/Navigate';
import {getActiveLink} from '../../utils/activeLink';
import {getUserIcon} from '../../utils/userIcon';
import {logout, propose, search, signin, signup,} from "../../utils/dictionary";
import {
  getMyNotifications,
  getNotifications,
  markAsRead
} from "../../Domain/NotificationLibrary";

/**
 * Render the Navbar which is styled by using Bootstrap
 * Each item in the Navbar is tightly coupled with the Router configuration :
 * - the URI associated to a page shall be given in the attribute "data-uri" of the Navbar
 * - the router will show the Page associated to this URI when the user click on a nav-link
 */

const Navbar = async () => {
  const navbarWrapper = document.querySelector('#navbarWrapper');
  const user = getAuthenticatedUser();
  const active = getActiveLink();
  const userIcon = getUserIcon();

  if (user === undefined) {
    const navbar = `
    <div class="navbar navbar-expand-lg bg-light navbar-light py-3 py-lg-0 px-0">
        <nav class="collapse navbar-collapse justify-content-around" id="navbarCollapse">          
                <div class="navbar-nav mr-auto py-0">
                    
                    <div id="homePage">

                    </div>
                    
                </div>
                <div class="navbar-nav ml-auto py-0">
                    <div id="proposePage">
                            
                    </div>
                    <div id="loginPage">
                        
                    </div>
                    <div id="registerPage">
                        
                    </div>
                </div>
        </nav>
    </div>
    
    `;
    navbarWrapper.innerHTML = navbar;
    if (active === 'homePage') {
      // active home
      const activeD = document.getElementById('homePage');
      activeD.innerHTML += `
         
          <h1  class="nav-item nav-link active " data-uri="/"  id="homeLogo"> RessourceRie </h1>
          
        `;
    }
    if (active !== 'homePage') {
      // inactive home
      const activeD = document.getElementById('homePage');
      activeD.innerHTML += `
          <h1 class="nav-item nav-link active " data-uri="/"  id="homeLogo"> RessourceRie </h1>
      `;
    }

    if (active === 'proposePage') {
      // active search
      const activeD = document.getElementById('proposePage');
      activeD.innerHTML += `
              <a href="#" class="nav-item nav-link active" data-uri="/propose"><i class="bi bi-plus"></i> ${propose}</a>
            `;
    }
    if (active !== 'proposePage') {
      // active search
      const activeD = document.getElementById('proposePage');
      activeD.innerHTML += `
              <a href="#" class="nav-item nav-link" data-uri="/propose"><i class="bi bi-plus"></i> ${propose}</a>
            `;
    }

    if (active === 'loginPage') {
      // active login
      const activeD = document.getElementById('loginPage');
      activeD.innerHTML += `
          <a href="#" class="nav-item nav-link active" data-uri="/login"><i class="bi bi-box-arrow-in-right"></i> ${signin}</a>
      `;
    }
    if (active !== 'loginPage') {
      // inactive login
      const activeD = document.getElementById('loginPage');
      activeD.innerHTML += `
          <a href="#" class="nav-item nav-link" data-uri="/login"><i class="bi bi-box-arrow-in-right"></i> ${signin}</a>
      `;
    }
    if (active === 'registerPage') {
      // inactive register
      const activeD = document.getElementById('registerPage');
      activeD.innerHTML += `
          <a href="#" class="nav-item nav-link active" data-uri="/register"><i class="bi bi-person-plus"></i> ${signup}</a>
      `;
    }
    if (active !== 'registerPage') {
      // inactive register
      const activeD = document.getElementById('registerPage');
      activeD.innerHTML += `
          <a href="#" class="nav-item nav-link" data-uri="/register"><i class="bi bi-person-plus"></i> ${signup}</a>
      `;
    }
  } else {

    const navbar = `

        <div class="navbar navbar-expand-lg bg-light navbar-light py-3 py-lg-0 px-0">
            <nav class="collapse navbar-collapse justify-content-around" id="navbarCollapse">
                    <div class="navbar-nav mr-auto py-0">

                        <div id="homePage">

                        </div>

                    </div>
                    <div class="navbar-nav ml-auto py-0">
                        <div id="searchPage">

                        </div>
                        <div id="proposePage">

                        </div>
                        <div id="notificationBell">
                            
                        </div>
                        <div id="userPage">
                            
                        </div>
                        <div id=extUserPage">
                            
                        </div>
                        <a href="#" class="nav-item nav-link" data-uri="/logout"><i class="bi bi-box-arrow-right"></i> ${logout}</a>
                    </div>
            </nav>
        </div>
       
        `;
    navbarWrapper.innerHTML = navbar;

    if (active === 'homePage') {
      // active home
      const activeD = document.getElementById('homePage');
      activeD.innerHTML += `
          <h1 class="nav-item nav-link active " data-uri="/"  id="homeLogo"> RessourceRie </h1>
        `;
    }
    if (active !== 'homePage') {
      // inactive home
      const activeD = document.getElementById('homePage');
      activeD.innerHTML += `
          <h1 class="nav-item nav-link active " data-uri="/"  id="homeLogo"> RessourceRie </h1>
      `;
    }

    // icon thing
    if (userIcon === 'userPage') {
      // active user
      const activeD = document.getElementById('userPage');
      activeD.innerHTML += `
          <a id="user" class="nav-item nav-link active" ><i class="bi bi-person-fill"></i> ${user.firstname} ${user.lastname}</a>
      `;
    } else if (userIcon === 'extUserPage') {
      // inactive extern user
      const activeD = document.getElementById('userPage');
      activeD.innerHTML += `
          <a id="user" class="nav-item nav-link"><i class="bi bi-person"></i> ${user.firstname} ${user.lastname}</a>
      `;
    } else if (userIcon === 'adminPage') {
      // active user
      const activeD = document.getElementById('userPage');
      activeD.innerHTML += `
          <a id="user" class="nav-item nav-link active" ><i class="bi bi-incognito"></i> ${user.firstname} ${user.lastname}</a>
      `;
    } else if (userIcon === 'helperPage') {
      // active user
      const activeD = document.getElementById('userPage');
      activeD.innerHTML += `
          <a id="user" class="nav-item nav-link active" ><i class="bi bi-person-check-fill"></i> ${user.firstname} ${user.lastname}</a>
      `;
    }

    if (user.userRole !== "USER") {
      // adds to icon thing
      if (active === 'userPage') {
        // active user
        const activeD = document.getElementById('user');
        activeD.classList.add('active');
      }

      if (active !== 'searchPage') {
        // active search
        const activeD = document.getElementById('searchPage');
        activeD.innerHTML += `
              <a href="#" class="nav-item nav-link" data-uri="/search"><i class="bi bi-search"></i> ${search}</a>
            `;
      }
    }
    if (active === 'proposePage') {
      // active search
      const activeD = document.getElementById('proposePage');
      activeD.innerHTML += `
              <a href="#" class="nav-item nav-link active" data-uri="/propose"><i class="bi bi-plus"></i> ${propose}</a>
            `;
    }
    if (active !== 'proposePage') {
      // active search
      const activeD = document.getElementById('proposePage');
      activeD.innerHTML += `
              <a href="#" class="nav-item nav-link" data-uri="/propose"><i class="bi bi-plus"></i> ${propose}</a>
            `;
    }

    const notifBell = document.getElementById('notificationBell');
    notifBell.innerHTML = `
          <div class="btn-group">
            <button id="notifBell" type="button" class="nav-item nav-link dropdown-toggle" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="false">
              <i class="bi bi-bell"></i>
            </button>
            <ul class="dropdown-menu dropdown-menu-lg-end" id="listNotif">
            </ul>
          </div>
      `;

  }
  // fin else

  const userBtn = document.getElementById('user');
  userBtn?.addEventListener('click', async (e) => {
    e.preventDefault();
    Navigate('/profile');
  });

  const userRole = await user.userRole;
  if (userRole === "USER") {
    const listNotifications = await getMyNotifications(user.id)
    const listNotif = document.getElementById('listNotif');
    // eslint-disable-next-line no-plusplus
    listNotif.innerHTML = ``;
    let atLeastOneNotif = false;
    // eslint-disable-next-line no-plusplus
    for (let i = 0; i < listNotifications.length; i++) {
      if (atLeastOneNotif === false) {
        atLeastOneNotif = true;
      }
      // const notifDate = relativeTime(listNotifications[i].notification_date);
      listNotif.innerHTML += `
                  <li>
                    <span>${listNotifications[i].content}</span>
                  </li>
                `;
    }// fin for
    if (atLeastOneNotif === false) {
      listNotif.innerHTML = `
      <p class="dropdown-item" >Aucune notification pour l'instant!</p>
    `;
    }
  } else {
    const listNotifications = await getNotifications()
    const listNotif = document.getElementById('listNotif');
    // eslint-disable-next-line no-plusplus
    listNotif.innerHTML = ``;
    let atLeastOneNotif = false;
    // eslint-disable-next-line no-plusplus
    for (let i = 0; i < listNotifications.length; i++) {
      if (listNotifications[i].state === "UNREAD") {
        if (atLeastOneNotif === false) {
          atLeastOneNotif = true;
        }
        // const notifDate = relativeTime(listNotifications[i].notification_date);

        listNotif.innerHTML += `
                  <li>
                    <span>${listNotifications[i].content}</span>
                    <button name="${listNotifications[i].id}" class="markRead dropdown-item" type="button">Marquer comme lu</button>
                  </li>
                `;
      }

      // permet le render vers la page du objet cliqué
      const markReadbtns = document.getElementsByClassName('markRead');
      for (let j = 0; j < markReadbtns.length; j += 1) {
        markReadbtns[j].addEventListener('click', async (e) => {
          e.preventDefault();
          const id = markReadbtns[j].name;
          // eslint-disable-next-line prefer-template
          await markAsRead(id);
        });
      }
    }// fin for
    if (atLeastOneNotif === false) {
      listNotif.innerHTML = `
      <p class="dropdown-item" >Aucune notification pour l'instant!</p>
    `;
    }
  }
  ;

}

export default Navbar;