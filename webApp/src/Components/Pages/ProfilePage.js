/* eslint-disable no-console */
import 'bootstrap/dist/css/bootstrap.min.css';
import {clearPage} from '../../utils/render';
import {setActiveLink} from '../../utils/activeLink';
import {setUserIcon} from '../../utils/userIcon';
import Navbar from '../Navbar/Navbar';
import {getAuthenticatedUser} from "../../utils/auths";
import Navigate from "../Router/Navigate";
import {
  displayAllMyObjects,
  updateResponsibleDisponibility,
  updateUserInfo
} from "../../utils/dictionary";
import {
  getAllUsers,
  makeUserHelper,
  saveUserInfo
} from "../../Domain/UserLibrary";
import {addAvailabilities} from "../../Domain/AvailabilityLibrary";

const indiquerdates = `
<div id="message">
              
</div>
      <div class="p-md-5 mx-md-4">
         <div class="text-center">
            <h1>Formulaire d'ajout de disponibilités!</h1>  
            <h4>Veuillez introduire une nouvelle disponibilité</h4>                                      
         </div>
             <input type="number" class="form-control" id="annee" placeholder="Entrez l'année (AAAA)">
             <input type="number" class="form-control" id="mois" placeholder="Entrez le mois (MM)">
             <input type="number" class="form-control"  id="jour" placeholder="Entrez le jour (DD)">
         </div> 
         
         <div>
            <label>Choisissez le moment de la journée</label>
            <select id="timeSlotChoix">
                <option disabled>Choisir</option>
                <option value="matin">Matin</option>
                <option value="apres-midi">Après-midi</option>
            </select>
         </div>
         
         <div class="text-center pt-1 mb-5 pb-1">
             <button class="btn btn-success rounded-pill" id="saveDisponibilitesBtn" type="button" style="color : white">Enregistrer</button>
         </div> 
                             
      </div>
    `;
const ProfilePage = () => {
  const user = getAuthenticatedUser();
  const html = `
    <div class="row">
      <div class="col-md-2 border-right">
        <div class="d-flex flex-column align-items-center text-center p-3 py-5">
          <img class="rounded-circle mt-3" width="150px" src="http://localhost:8080/users/load/${user.id}">
          <div id="userInfo">
            <span class="font-weight-bold">${user.firstname} ${user.lastname}</span><br>
            <span class="text-black-50">${user.email}</span>
          </div>

        </div>
        <div class="d-flex flex-column align-items-center text-center p-3 py-5">
          <div id="mainUserButtons">
            <div class="text-center pt-1 mb-3 pb-1">
              <button class="btn btn-outline-dark rounded-pill " id="updateResponsibleDisponibility" type="button"  style="display: none;" >${updateResponsibleDisponibility}</button>
            </div>
            <div class="text-center pt-1 mb-2 pb-1">
              <div id="btnUpdateInfo">
                <button class="btn btn-success rounded-pill" id="updateUserInf" type="button" >${updateUserInfo}</button>
              </div>
            </div>
            <div class="text-center pt-1 mb-3 pb-1">
              <div id="myObjectsUser">
              <button class="btn btn-success rounded-pill" id="getAllMyObjects" type="button">${displayAllMyObjects}</button>
              </div>
            </div>
            <div class="text-center pt-1 mb-3 pb-1">
              <div id="displayUsers">
              <button class="btn btn-success rounded-pill" id="displayuserslist" type="button" style="display: none;">Afficher la liste des utilisateurs</button>
              </div>
            </div>
            <div class="text-center pt-1 mb-3 pb-1">
              <div id="dashboard">
                  <button class="btn btn-success rounded-pill" id="dashboardbtn" type="button" style="display: none;">Afficher le tableau de bord</button>
               </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div id="responsibles">

      </div>
      <div id="myObjects">
      
      </div>
      <div id="message"></div>  
    </div>
`;

  if (user === undefined) {
    Navigate('/login');
  }
  clearPage();
  setActiveLink('userPage');

  // if pour affichage de la page si admin ou aidant
  if (user.userRole === "RESPONSIBLE" || user.userRole === "HELPER") {

    // code commun aux 2

    setUserIcon('helperPage');
    Navbar();
    const main = document.querySelector('main');
    main.innerHTML = html;
    updateUserInfoBtn(user);

    const updateResponsibleDisponibilitybtn = document.getElementById(
        'updateResponsibleDisponibility');
    updateResponsibleDisponibilitybtn.addEventListener('click', (e) => {
      e.preventDefault();
      main.innerHTML = indiquerdates;
      const saveDisponibilitesBtn = document.getElementById(
          'saveDisponibilitesBtn');
      saveDisponibilitesBtn.addEventListener('click', (d) => {
        d.preventDefault();
        addAvailabilities();
      })
    })

    const dispo = document.getElementById('updateResponsibleDisponibility');
    dispo.style.display = 'block';

    const btnDashboard = document.getElementById('dashboardbtn');
    btnDashboard.style.display = 'block';
    btnDashboard.addEventListener('click', () => {
      Navigate("/dashboard");
    });

    if (user.userRole === "RESPONSIBLE") {
      const btnDisplauUser = document.getElementById('displayuserslist');
      btnDisplauUser.style.display = 'block';
      setUserIcon('adminPage');
      Navbar();

    }
  } else { // page affiché pour un utilisateur basique
    setUserIcon('userPage');
    Navbar();
    const main = document.querySelector('main');
    main.innerHTML = html;
    updateUserInfoBtn(user);
  }

  const getAllMyObjectsBtn = document.getElementById('getAllMyObjects');

  getAllMyObjectsBtn.addEventListener('click', () => {
    Navigate('/myObjects');
  });

  const getUserListBtn = document.getElementById('displayuserslist');

  getUserListBtn.addEventListener('click', () => {
    Navigate('/users');
  });

};

function updateUserInfoBtn(user) {
  const btn = document.getElementById('updateUserInf');

  btn.addEventListener('click', async (e) => {
    e.preventDefault();
    const buttons = document.getElementById('mainUserButtons');
    Navbar();
    const main = document.querySelector('main');

    main.innerHTML = `
            <div id="message"></div>
            <div class="align-content-center">
               <div class="col-md-12"><input type="text" class="form-control" id="newFirstname" value="${user.firstname}" placeholder="Prénom..." ></div>
               <div class="col-md-12"><input type="text" class="form-control" id="newLastname" value="${user.lastname}" placeholder="Nom..."></div>
               <div class="col-md-12"><input type="text" class="form-control" id="newEmail" value="${user.email}" placeholder="Email..." ></div>
               <div class="col-md-12"><input type="text" class="form-control" id="newGsm" value="${user.gsm}" placeholder="GSM..." ></div>
               <div class="col-md-12"><input type="text" class="form-control" id="oldPassword" value="" placeholder="Mot de passe actuel" ></div>
               <div class="col-md-12"><input type="text" class="form-control" id="oldPassword2" value="" placeholder="Répétez le mot de passe actuel" ></div>
               <div class="col-md-12"><input type="text" class="form-control" id="newPassword" value="" placeholder="Nouveau mot de passe si besoin" ></div>
            </div> 
        `;
    buttons.innerHTML = `
            <div class="text-center pt-1 mb-0 pb-1">
                <button class="btn btn-outline-success profile-button" id="saveUserInf" type="button">Enregistrer les modifications ! </button>
            </div>
        `;
    main.appendChild(buttons);
    await saveUserInfo();
  });
}

// eslint-disable-next-line no-unused-vars
async function showUsers(user) {
  const userListDiv = document.getElementById('users');
  const usersList = await getAllUsers();
  usersList.forEach((element) => {

    userListDiv.innerHTML += `
                <div class="bg-black">
                    <div class="d-flex flex-column align-items-center text-center p-3 py-5">
                        <div id="ObjectPhoto">
                            <img class="rounded-circle mt-3" width="50px" src="http://localhost:8080/users/load/${element.id}">
                        </div> 
                    </div>
                    <div class="d-flex flex-column p-2 py-5">
                        <p class="display-6">${element.firstname} ${element.lastname}</p><br>
                        <p>${element.email}</p>
                        <div class="makehelperDiv">
                        
                        </div>
                        
                    </div>
                </div>
                <br><br>
            `;

  });

  const makeHelperDiv = document.getElementsByClassName('makehelperDiv');

  for (let j = 0; j < makeHelperDiv.length; j += 1) {

    if (usersList[j].userRole === "USER") {
      makeHelperDiv[j].innerHTML += `
                <button class="btn btn-primary rounded-pill makeHelp " id="makeHelper" name="${usersList[j].id}">Indiquer comme aidant</button>
            `;
    }
  } // fin for

  // permet le render vers la page du objet cliqué
  const a = document.getElementsByClassName('makeHelp');

  const lengthUsers = a.length;
  for (let j = 0; j < lengthUsers; j += 1) {

    a[j].addEventListener('click', async (e) => {
      e.preventDefault();
      const id = a[j].name;
      // eslint-disable-next-line prefer-template
      makeUserHelper(id);

    });
  } // fin for
}

export default ProfilePage;