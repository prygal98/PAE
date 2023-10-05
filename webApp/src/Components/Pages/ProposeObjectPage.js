/* eslint-disable no-console */
import 'bootstrap/dist/css/bootstrap.min.css';
import {clearPage} from '../../utils/render';
import {setActiveLink} from '../../utils/activeLink';
import {setUserIcon} from '../../utils/userIcon';
import Navbar from '../Navbar/Navbar';
import Navigate from "../Router/Navigate";
import {getAuthenticatedUser} from "../../utils/auths";
import {getAvailabilities} from "../../Domain/AvailabilityLibrary";
import {renderPopUp} from "../../utils/utilsForm";
import {addObject, getTypesObjet} from "../../Domain/ObjectsLibrary";
// import {addObject} from "../../Domain/ObjectsLibrary";

const html = `
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-xl-10">
                    <div class="card rounded-3 text-black" id="backseparator">
                        <div class="row g-0">
                        
                            <div id="contenuProposition">
                                
                                
                            </div>                         
                        </div>
                    </div>
                </div>
            </div>   
`;

const etapeUn = `
<div id="message">
              
</div>
      <div class="p-md-5 mx-md-4">
         <div class="text-center">
            <h1>Veuillez entrer votre numéro de GSM!</h1>                                        
         </div>
         
         <div class="d-flex align-items-center justify-content-center pb-4">
            <p class="mb-0 me-2">Ou bien créez vous un compte.</p>
            <button type="button" class="btn btn-outline-primary rounded-pill" id="register">Cliquez ici</button>
         </div>
         
         <div class="form-outline mb-4">
             <input type="text" class="form-control" placeholder="Entrez ici votre numéro de GSM..." id="GSMProposition">
         </div> 
         
         <div class="text-center pt-1 mb-5 pb-1">
             <button class="btn btn-success rounded-pill" id="UnregisteredStepOne" type="button" style="color : white">Etape suivante</button>
         </div> 
                                      
      </div>
    `;

const etapeDeu = `
<div id="message">
              
</div>
      <div class="p-md-5 mx-md-4">
         <div class="text-center">
            <h1>Choisissez une date libre!</h1>                                        
         </div>
         
         <div class="d-flex align-items-center justify-content-center pb-4">
            <p class="mb-0 me-2">Veuillez préciser à quel moment souhaitez-vous apporter votre objet.</p>
         </div>
         
         <div id="availabilitiesDate">
            
         </div>
         <div id="availabilitiesTime">
         
         </div>
         
         <div class="text-center pt-1 mb-5 pb-1">
             <button class="btn btn-success rounded-pill" id="UnregisteredStepTwo" type="button" style="color : white">Etape suivante</button>
         </div> 
         
         <div class="d-flex align-items-center justify-content-center pb-4">
            <p class="mb-0 me-2 text-center">Ceci sont les dates auxquelles Monsieur Riez ou ses aidants seront présents afin de récuperer votre objet au parc à conteneurs.</p>
         </div>
                                      
      </div>
    `;

const etapeTrois = `
<div id="message">
              
</div>
      <div class="p-md-5 mx-md-4">
         <div class="text-center">
            <h1>Ajoutez votre objet!</h1>                                        
         </div>
                                        <div class="form-outline mb-4">
                                            <label class="form-label" for="pfp">La photo de votre objet</label>
                                            <input type="file" accept="image/*" id="objectphoto" class="form-control"
                                                placeholder="Choisissez le photo de votre objet..." />
                                        </div>
                                        
                                        <div class="form-outline mb-4">
                                            <label class="form-label" for="objectname">Nom</label>
                                            <input type="text" id="objectname" class="form-control"
                                                placeholder="Entrez votre nom de votre objet..." />
                                        </div>
                                        
                                        <div class="form-outline mb-4">
                                            <label class="form-label" for="types">Type d'objet</label>
                                            <select name="types" id="typedobjet">
                                              
                                            </select>
                                        </div>
                                        
                                        <div class="form-outline mb-4">
                                            <label class="form-label" for="descriptionObjet">Description</label>
                                            <textarea name="descripionduObjet" rows="4"  id="descriptionObjet" class="form-control" placeholder="Entrez la descripion de votre objet (Maximum 120 mots)..."></textarea>
                                        </div>

         <div class="text-center pt-1 mb-5 pb-1">
             <button class="btn btn-success rounded-pill" id="UnregisteredStepThree" type="button" style="color : white">Etape suivante</button>
         </div> 
         
         <div class="d-flex align-items-center justify-content-center pb-4">
            <p class="mb-0 me-2 text-center">Completez les champs pour ajouter votre objet</p>
         </div>
                                      
      </div>
    `;

const etapeQuattre = `
<div id="message">
              
</div>
      <div class="p-md-5 mx-md-4">
         <div class="text-center">
            <h1>Votre objet sera révisé dès que possible!</h1>                                        
         </div>

         <div class="text-center pt-1 mb-5 pb-1">
             <button class="btn btn-success rounded-pill" id="UnregisteredStepFour" type="button" style="color : white">Terminer</button>
         </div> 
         
         <div class="d-flex align-items-center justify-content-center pb-4">
            <p class="mb-0 me-2 text-center">Ce processus peut prendre de quelques minutes à quelques jours... <br
            Vous en serez notifié.</p>
         </div>
                                      
      </div>
    `;

const ProposeObjectPage = () => {
  const user = getAuthenticatedUser();
  clearPage();
  setActiveLink('proposePage');
  setUserIcon('extUserPage');
  Navbar();
  const main = document.querySelector('main');
  main.innerHTML += html;
  if (user === undefined) {
    stepone();
  } else {
    steptwo(undefined, user);
  }
};

async function stepone() {
  const contenuProposition = document.getElementById('contenuProposition');
  contenuProposition.innerHTML = etapeUn;
  const UnregisteredStepOne = document.getElementById('UnregisteredStepOne');

  const registerbtn = document.getElementById("register");

  registerbtn.addEventListener('click', (e) => {
    e.preventDefault();
    Navigate('/register');
  })
  UnregisteredStepOne.addEventListener('click', (e) => {
    e.preventDefault();
    const GSMProposition = document.getElementById('GSMProposition').value;
    if (GSMProposition.length < 10) {
      const message = document.getElementById('message');
      message.innerHTML = `<div id="snackbar">Entrez un numéro valide (10 numéros) avant de passer à la prochaine étape!</div>`;
      renderPopUp();
    } else {
      steptwo(GSMProposition, undefined);
    }
  });
}

async function steptwo(gsm, user) {
  if (user !== undefined && gsm === undefined) {
    // eslint-disable-next-line no-param-reassign
    gsm = user.gsm;
  }
  const contenuProposition = document.getElementById('contenuProposition');
  contenuProposition.innerHTML = etapeDeu;
  const availabilitiesDate = document.getElementById('availabilitiesDate');
  // const availabilitiesTime = document.getElementById('availabilitiesTime');
  const availabilities = await getAvailabilities();
  let cpt = 0;
  availabilities.forEach((element) => {
    console.log(element);
    availabilitiesDate.innerHTML += `
        <label><input type="radio" class="availabilityDate" name="dateLibre"  id="${cpt}" value="${element.id}">  ${element.availability[2]}/${element.availability[1]}/${element.availability[0]} - ${element.timeSlot}</label>
        <br>
    `;
    // eslint-disable-next-line no-plusplus
    cpt++;
  });

  const UnregisteredStepTwo = document.getElementById('UnregisteredStepTwo');

  UnregisteredStepTwo.addEventListener('click', (e) => {
    e.preventDefault();
    const availabilityChecked = document.getElementsByClassName(
        'availabilityDate');
    console.log('trcu availibitly', availabilityChecked);
    let availabilityCheck = false;
    // eslint-disable-next-line no-plusplus
    for (let i = 0; i < availabilityChecked.length; i++) {
      if (availabilityChecked[i].checked === true) {
        availabilityCheck = availabilityChecked[i];
        break;
      } else {
        availabilityCheck = false;
      }
    }
    if (availabilityCheck === false) {
      const message = document.getElementById('message');
      message.innerHTML = `<div id="snackbar">Veuillez choisir une date avant de passer à la prochaine étape!</div>`;
      renderPopUp();
    } else {
      stepthree(gsm, availabilityCheck.value);
    }
  });
}

async function stepthree(gsm, availability) {
  const contenuProposition = document.getElementById('contenuProposition');
  contenuProposition.innerHTML = etapeTrois;
  const types = await getTypesObjet();
  console.log('les types', types);
  const typedobjet = document.getElementById('typedobjet');
  types.forEach((element) => {
    typedobjet.innerHTML += `
    <option value="${element.id}">${element.type}</option>
  `;
  });

  const UnregisteredStepThree = document.getElementById(
      'UnregisteredStepThree');

  UnregisteredStepThree.addEventListener('click', (e) => {
    e.preventDefault();
    const objectname = document.getElementById("objectname").value;
    const descriptionObjet = document.getElementById('descriptionObjet').value;
    const objectphoto = document.getElementById("objectphoto");
    const typeobj = document.getElementById("typedobjet").value;
    const mots = document.getElementById("descriptionObjet").value.trim().split(
        /\s+/);
    if (mots.length > 120) {
      const message = document.getElementById('message');
      message.innerHTML = `<div id="snackbar">La description est limitée à 120 caractères.</div>`;
      renderPopUp();
    } else if (objectname.length === 0 || typeobj.length === 0
        || descriptionObjet.length === 0) {
      const message = document.getElementById('message');
      message.innerHTML = `<div id="snackbar">Veuillez compléter les champs avant de passer à la prochaine étape!</div>`;
      renderPopUp();
    } else {

      contenuProposition.innerHTML = etapeQuattre;
      stepfour(gsm, availability, objectname, typeobj, descriptionObjet,
          objectphoto);
    }
  });
}

function stepfour(gsm, availability, objectname, type, description,
    objectphoto) {
  const UnregisteredStepFour = document.getElementById('UnregisteredStepFour');
  UnregisteredStepFour.addEventListener('click', (e) => {
    e.preventDefault();
    addObject(gsm, availability, objectname, type, description, objectphoto);
    Navigate('/');
  });
}

export default ProposeObjectPage;