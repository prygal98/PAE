/* eslint-disable no-console */
import 'bootstrap/dist/css/bootstrap.min.css';
import {clearPage} from '../../utils/render';
import {clearActive, setActiveLink} from '../../utils/activeLink';
import {setUserIcon} from '../../utils/userIcon';
import Navbar from '../Navbar/Navbar';
import {getAllObjectsInStore, getTypesObjet} from "../../Domain/ObjectsLibrary";
import Navigate from "../Router/Navigate";
import {getAuthenticatedUser} from "../../utils/auths";
import {renderPopUp} from "../../utils/utilsForm";

const html = `
<div class="container text-center my-3" >
    <h2 class="font-weight-light">Bienvenue sur RessourceRie</h2>
    <div id="ifobjects">
    </div>
</div>
`;

const HomePage = async () => {
  clearPage();
  setActiveLink('homePage');
  setUserIcon('extUserPage');
  Navbar();

  const main = document.querySelector('main');
  main.innerHTML = html;

  const objects = await getAllObjectsInStore();
  const ifobjects = document.getElementById('ifobjects');

  if (objects.length === 0 || objects === undefined) {
    ifobjects.innerHTML = `
          <h5 class="mt-2 fw-light">Il n'y a pas d'objets dans le magasin!</h5>
        `;
  } else {
    ifobjects.innerHTML = `
            <div class="form-outline mb-4">
                  <label class="form-label" for="types">Trier par type d'objet : </label>
                  <select name="types" id="typedobjet">
                      <option selected>Choisissez un type d'objet</option>                                
                  </select>
            </div>
            <div class="form-outline mb-4">
                  <label class="form-label" for="states">Afficher les objets :</label>
                  <select name="states" id="statedobjet">
                      <option selected>Choisissez un etat</option>                                
                  </select>
            </div>
            <div id="message"></div>
            <div class="row mx-auto my-auto justify-content-center">
                  <div id="recipeCarousel" class="carousel slide" data-bs-ride="carousel">
                      <div class="carousel-inner" role="listbox" id="cardsObjets">
                          
                             
                      </div>
                      <a class="carousel-control-prev w-aut" href="#recipeCarousel" role="button" data-bs-slide="prev">
                          <span class="carousel-control-prev-icon bg-black" aria-hidden="true"></span>
                      </a>
                      <a class="carousel-control-next   w-aut" href="#recipeCarousel" role="button" data-bs-slide="next">
                          <span class="carousel-control-next-icon bg-black" aria-hidden="true"></span>
                      </a>
                  </div>
            </div>
        `;

    const typedobjet = document.getElementById('typedobjet');

    // TYPES
    const types = await getTypesObjet();
    types.forEach((element) => {
      typedobjet.innerHTML += `
                <option value="${element.id}">${element.type}</option>
              `;
    });
    // STATE
    const statedobjets = document.getElementById('statedobjet');

    // TYPES
    const states = ['STORE', 'SALE', 'SOLD'];
    const stateFR = ['En magasin', 'Mis en vente', 'Vendu']
    // TYPES
    // eslint-disable-next-line no-plusplus
    for (let i = 0; i < states.length; i++) {
      const state = states[i];
      statedobjets.innerHTML += `<option value="${state}">${stateFR[i]}</option> `;
    }

    const a = document.getElementsByClassName('gotoobjectPagebtn');
    const lengthObjects = a.length;
    for (let j = 0; j < lengthObjects; j += 1) {
      a[j].addEventListener('click', async (e) => {
        e.preventDefault();
        const id = a[j].name;
        // eslint-disable-next-line prefer-template
        clearActive();
        Navigate('/object?id=', id);
      });
    }

    showCarousel(objects);

    // OBJECTS
    typedobjet.addEventListener('change', async (e) => {
      e.preventDefault();
      const newObjectList = objects.filter(
          (obj) => parseInt(obj.typeObject, 10) === parseInt(typedobjet.value,
              10));
      if (newObjectList.length === 0) {
        const message = document.getElementById('message');
        message.innerHTML = `<div id="snackbar">Aucun objet n'a été trouvé pour le type séléctionné.</div>`;
        renderPopUp();
        showCarousel(objects);
      } else {
        showCarousel(newObjectList);
      }
      initializeCarousel();
    });

    statedobjets.addEventListener('change', async (e) => {
      e.preventDefault();
      const newObjectList = objects.filter(
          (obj) => obj.state === statedobjets.value);
      if (newObjectList.length === 0) {
        const message = document.getElementById('message');
        message.innerHTML = `<div id="snackbar">Aucun objet n'a été trouvé pour l'etat séléctionné.</div>`;
        renderPopUp();
        showCarousel(objects);
      } else {
        showCarousel(newObjectList);
      }
      initializeCarousel();
    });

    initializeCarousel()

  }
};

function initializeCarousel() {
  const items = document.querySelectorAll('.carousel .carousel-item');
  items.forEach((el) => {
    const minPerSlide = 4;
    let next = el.nextElementSibling;
    // eslint-disable-next-line no-plusplus
    for (let i = 1; i < minPerSlide; i++) {
      if (!next) {
        // eslint-disable-next-line prefer-destructuring
        next = items[0];
      }
      const cloneChild = next.cloneNode(true);
      el.appendChild(cloneChild.children[0]);
      next = next.nextElementSibling;
    }
  });
}

// eslint-disable-next-line no-unused-vars
async function getListeObjets() {
  const typeList = await getTypesObjet();
  const objets = typeList.map((type) => ({
    id: type.id,
    type: type.type
  }));
  return objets;
}

async function showCarousel(objects) {

  const cardObjets = document.getElementById('cardsObjets');
  cardObjets.innerHTML = `
      <div class="carousel-item active">
        ${renderObjectCard(objects[0])}
      </div>
    `;

  // eslint-disable-next-line no-plusplus
  for (let i = 1; i < objects.length; i++) {
    cardObjets.innerHTML += `
        <div class="carousel-item">
          ${renderObjectCard(objects[i])}
        </div>
      `;
  }

  const user = getAuthenticatedUser();
  const carouselItems = document.querySelectorAll('.carousel-item');
  carouselItems.forEach((item) => {
    item.addEventListener('click', () => {
      // eslint-disable-next-line no-restricted-globals
      const objectId = event.target.closest('.object-element')?.getAttribute(
          'data-id');
      if (user !== undefined) {
        Navigate(`/object?id=${objectId}`);
      }
    });
  });
}

function renderObjectCard(object) {
  const price = object.sellingPrice === 0 ? `- €`
      : `Prix : ${object.sellingPrice} €`;
  const objectElement = document.createElement('div');
  objectElement.classList.add('col-md-3', 'object-element');
  objectElement.setAttribute('data-id', object.id);

  const cardElement = document.createElement('div');
  cardElement.classList.add('card');
  // Add border color based on object state
  if (object.state === 'STORE') {
    cardElement.classList.add('border-success');
  } else if (object.state === 'SALE') {
    cardElement.classList.add('border-success');
  } else if (object.state === 'SOLD') {
    cardElement.classList.add('border-danger');
  }
  // Add rounded corners and thickness to card

  cardElement.style.borderWidth = '2px';
  // Add more transparency to border color for rounded corners
  cardElement.style.borderColor = 'rgba(0, 0, 0, 0,5)';
  objectElement.appendChild(cardElement);

  const cardImgElement = document.createElement('div');
  cardImgElement.classList.add('card-img');
  cardElement.appendChild(cardImgElement);

  const imgElement = document.createElement('img');
  imgElement.src = `http://localhost:8080/objects/load/${object.id}`;
  imgElement.classList.add('img-fluid');
  // Add click event listener to object element
  cardImgElement.appendChild(imgElement);

  const cardOverlayElement = document.createElement('div');
  cardOverlayElement.classList.add('card-img-overlay', 'd-flex',
      'flex-column', 'justify-content-end');
  cardElement.appendChild(cardOverlayElement);

  const textWrapperElement = document.createElement('div');
  textWrapperElement.classList.add('text-wrapper');
  textWrapperElement.style.backgroundColor = 'rgba(255, 255, 255, 0.7)';
  textWrapperElement.style.padding = '10px';
  textWrapperElement.style.color = 'black';
  // Add rounded corners and thickness to text wrapper
  textWrapperElement.style.borderRadius = '10px';
  textWrapperElement.style.borderWidth = '2px';
  textWrapperElement.style.borderStyle = 'solid';
  textWrapperElement.style.borderColor = 'black';
  // Add more transparency to border color for rounded corners
  textWrapperElement.style.borderColor = 'rgba(0, 0, 0, 0.3)';
  cardOverlayElement.appendChild(textWrapperElement);

  const nameElement = document.createElement('p');
  nameElement.textContent = object.name;
  textWrapperElement.appendChild(nameElement);

  const typeElement = document.createElement('p');

  const liste = [
    {id: 1, type: 'Meuble'},
    {id: 2, type: 'Table'},
    {id: 3, type: 'Chaise'},
    {id: 4, type: 'Fauteuil'},
    {id: 5, type: 'Lit/Sommier'},
    {id: 6, type: 'Matelas'},
    {id: 7, type: 'Couvertures'},
    {id: 8, type: 'Matériel de cuisine'},
    {id: 9, type: 'Vaisselle'},
  ];
  let typE;
  const typeObject = liste.find(type => type.id === object.typeObject);
  if (typeObject) {
    typE = typeObject.type;
  } else {
    typE = "Type inconnu";
  }

  typeElement.textContent = `Type : ${typE}`;
  textWrapperElement.appendChild(typeElement);

  const stateElement = document.createElement('p');
  if (object.state === 'STORE') {
    stateElement.textContent = 'En magasin';
  } else if (object.state === 'SALE') {
    stateElement.textContent = 'Mis en vente';
  } else if (object.state === 'SOLD') {
    stateElement.textContent = 'Vendu ! ';
    stateElement.style.color = 'red'; // Set text color to red for SOLD state
  }
  textWrapperElement.appendChild(stateElement);

  const priceElement = document.createElement('p');
  priceElement.textContent = price;
  textWrapperElement.appendChild(priceElement);

  return objectElement.outerHTML;
}

export default HomePage;