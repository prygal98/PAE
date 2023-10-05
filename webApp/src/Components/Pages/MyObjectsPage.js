import 'bootstrap/dist/css/bootstrap.min.css';
import { clearPage } from '../../utils/render';
import { setUserIcon } from '../../utils/userIcon';
import Navbar from '../Navbar/Navbar';
import { getAuthenticatedUser } from '../../utils/auths';
import Navigate from '../Router/Navigate';
import { getObjectsByUser, getTypesObjet } from '../../Domain/ObjectsLibrary';

const MyObjectsPage = async () => {
  const me = getAuthenticatedUser();
  if (me === undefined) {
    Navigate('/login');
  }

  clearPage();
  setUserIcon('extUserPage');
  Navbar();
  const main = document.querySelector('main');
  const divRow = document.createElement('div');

  if (me.userRole === 'RESPONSIBLE' || me.userRole === 'HELPER' || me.userRole ==='USER') {
    const divUserObjects = document.createElement('div');
    divUserObjects.setAttribute('id', 'userObjects');
    // Add title for user object list
    const objectTitle = document.createElement('h2');
    objectTitle.innerText = 'Mes objets';
    divUserObjects.appendChild(objectTitle);

    divRow.appendChild(divUserObjects);
    main.appendChild(divRow);
    const userId = me.id;
    const getObjectOfUserById = await getObjectsByUser(userId);

    const objectsContainer = document.createElement('div');
    objectsContainer.classList.add('objects-container');
    objectsContainer.style.display = 'flex';
    objectsContainer.style.flexWrap = 'wrap';
    objectsContainer.style.justifyContent = 'center';
    objectsContainer.style.marginTop = '20px'; // add some margin at the top

    // Retrieve the list of object types from the server
    const typeObjectsList = await getTypesObjet();

    // Render each object in the list
    getObjectOfUserById.forEach((object) => {
      const objectElement = document.createElement('div');
      objectElement.classList.add('object');

      // Appliquer les styles de bordure, couleur et arrière-plan à l'objet
      objectElement.style.border = '3px solid #8B0000'; // bordure plus grande, couleur rouge foncé
      objectElement.style.borderRadius = '7px'; // bordures arrondies
      objectElement.style.backgroundColor = 'rgba(255, 255, 255, 0.8)'; // fond légèrement transparent
      objectElement.style.boxShadow = '2px 2px 5px rgba(0, 0, 0, 0.3)'; // ajouter une ombre légère

      // Set width and margin for object element
      objectElement.style.width = '250px'; // adjust width as needed
      objectElement.style.margin = '10px'; // add some margin around each object

      // Create object image element
      const objectImage = document.createElement('img');
      objectImage.src = `http://localhost:8080/objects/load/${object.id}`;
      objectImage.alt = object.name;
      objectImage.classList.add('img-fluid');
      objectImage.style.width = '100%'; // set image width to fill the object element
      objectElement.appendChild(objectImage);

      // Create object name element
      const objectName = document.createElement('p');
      objectName.textContent = `Objet : ${object.name}`;
      objectElement.appendChild(objectName);

      // Create object type element
      const typeId = object.typeObject;
      const typeObject = typeObjectsList.find(type => type.id === typeId);
      const objectType = document.createElement('p');
      objectType.textContent = `Type: ${typeObject.type}`;
      objectElement.appendChild(objectType);

      // Create object ID element
      const objectDescription = document.createElement('p');
      objectDescription.textContent = `Description : ${object.description}`;
      objectElement.appendChild(objectDescription);

      // Create object price element
      const price = document.createElement('p');
      if (object.sellingPrice === 0){
        price.textContent = `- €`;
      }else {
        price.textContent = `Prix : ${object.sellingPrice} €`;
      }
      objectElement.appendChild(price);

      // Add click event listener to object element
      objectElement.addEventListener('click', () => {
        // Navigate to object page using object's ID
        Navigate(`/object?id=${object.id}`);
      });

      // Add object to container
      objectsContainer.appendChild(objectElement);
    });

// Add some space between the objects
    objectsContainer.style.marginTop = '10px';

// Render objects container
    divUserObjects.appendChild(objectsContainer);
  }
};

export default MyObjectsPage;
