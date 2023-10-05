import {clearPage} from '../../utils/render';
import {setActiveLink} from '../../utils/activeLink';
import {setUserIcon} from '../../utils/userIcon';
import Navbar from '../Navbar/Navbar';
import {getAuthenticatedUser} from '../../utils/auths';
import Navigate from '../Router/Navigate';
import {
  getAllObjects,
  getObjectsBetweenTwoDate,
  getTypesObjet
} from '../../Domain/ObjectsLibrary';

const FilterPage = async () => {
  const user = getAuthenticatedUser();
  if (user.userRole === 'RESPONSIBLE' || user.userRole === 'HELPER') {

    setActiveLink('userPage');
    setUserIcon('extUserPage');
    Navbar();

    const objectsList = await getAllObjects();
    const typeObjectsList = await getTypesObjet();
    const main = document.querySelector('main');
    clearPage();
    const objectsContainer = document.createElement('div');
    objectsContainer.classList.add('objects-container');

    // Create page title
    const pageTitle = document.createElement('h2');
    pageTitle.textContent = 'Recherche d\'objets';
    main.appendChild(pageTitle);

    // Create search input element
    const searchInput = document.createElement('input');
    searchInput.type = 'text';
    searchInput.placeholder = 'Recherche par nom ...';
    searchInput.addEventListener('input', () => {
      const searchText = searchInput.value.trim().toLowerCase();
      // eslint-disable-next-line no-use-before-define
      const minPrice = parseFloat(minPriceInput.value) || 0;
      // eslint-disable-next-line no-use-before-define
      const maxPrice = parseFloat(maxPriceInput.value)
          || Number.POSITIVE_INFINITY;
      const filteredObjects = objectsList.filter(obj =>
          obj.name.toLowerCase().startsWith(searchText) &&
          obj.sellingPrice >= minPrice &&
          obj.sellingPrice <= maxPrice
      );
      renderObjects(filteredObjects);
    });

    // Create min price input element
    const minPriceInput = document.createElement('input');
    minPriceInput.type = 'number';
    minPriceInput.placeholder = 'Prix minimum';
    minPriceInput.addEventListener('input', () => {
      const searchText = searchInput.value.trim().toLowerCase();
      const minPrice = parseFloat(minPriceInput.value) || 0;
      // eslint-disable-next-line no-use-before-define
      const maxPrice = parseFloat(maxPriceInput.value)
          || Number.POSITIVE_INFINITY;
      const filteredObjects = objectsList.filter(obj =>
          obj.name.toLowerCase().startsWith(searchText) &&
          obj.sellingPrice >= minPrice &&
          obj.sellingPrice <= maxPrice
      );
      renderObjects(filteredObjects);
    });

    // Create max price input element
    const maxPriceInput = document.createElement('input');
    maxPriceInput.type = 'number';
    maxPriceInput.placeholder = 'Prix maximum';
    maxPriceInput.addEventListener('input', () => {
      const searchText = searchInput.value.trim().toLowerCase();
      const minPrice = parseFloat(minPriceInput.value) || 0;
      const maxPrice = parseFloat(maxPriceInput.value)
          || Number.POSITIVE_INFINITY;
      const filteredObjects = objectsList.filter(obj =>
          obj.name.toLowerCase().startsWith(searchText) &&
          obj.sellingPrice >= minPrice &&
          obj.sellingPrice <= maxPrice
      );
      renderObjects(filteredObjects);
    });

    // Add some CSS styles to the search and price inputs
    searchInput.style.marginBottom = '10px';
    searchInput.style.padding = '5px';
    searchInput.style.border = '1px solid #ccc';
    searchInput.style.borderRadius = '4px';
    minPriceInput.style.marginLeft = '10px';
    maxPriceInput.style.marginLeft = '10px';

    // Render search and price inputs
    const filterInputs = document.createElement('div');
    filterInputs.appendChild(searchInput);
    filterInputs.appendChild(minPriceInput);
    filterInputs.appendChild(maxPriceInput);
    main.appendChild(filterInputs);

// Create title for type buttons
    const typeTitle = document.createElement('h4');
    typeTitle.textContent = 'Recherche par type ';
    main.appendChild(typeTitle);

// Render objects by type
    const renderObjectsByType = (type) => {
      const idType = typeObjectsList.find(value => value.type === type);
      const objectsByType = objectsList.filter(
          (obj) => obj.typeObject === idType.id);
      renderObjects(objectsByType);
    }

// Render objects by clicking on a type button
    const renderObjectsByTypeBtn = (typeBtn) => {
      typeBtn.addEventListener('click', () => {
        renderObjectsByType(typeBtn.textContent);
      });
    }

// Create type buttons
    const typeButtonsContainer = document.createElement('div');
    typeButtonsContainer.classList.add('type-buttons-container');
    typeObjectsList.forEach(typeObject => {
      const typeButton = document.createElement('button');
      typeButton.textContent = typeObject.type;
      typeButton.classList.add('type-button');
      typeButtonsContainer.appendChild(typeButton);

      renderObjectsByTypeBtn(typeButton);
    });

    main.appendChild(typeButtonsContainer);

// Create title for state buttons
    const stateTitle = document.createElement('h4');
    stateTitle.textContent = 'Recherche par état';
    main.appendChild(stateTitle);

// Render objects by state
    const renderObjectsByState = (state) => {
      const objectsByState = objectsList.filter(
          (obj) => obj.state === state);
      renderObjects(objectsByState);
    }

// Render objects by clicking on a state button
    const renderObjectsByStateBtn = (stateBtn) => {
      stateBtn.addEventListener('click', () => {
        renderObjectsByState(stateBtn.value);
      });
    }

// Create state buttons
    const stateButtonsContainer = document.createElement('div');
    stateButtonsContainer.classList.add('state-buttons-container');
    const states = ['STORE', 'OFFERED', 'CONFIRMED', 'DENIED', 'WORKSHOP',
      'SALE', 'SOLD', 'WITHDRAWN'];
    states.forEach(state => {
      const stateButton = document.createElement('button');
      stateButton.value = state;
      if (state === "STORE") {
        stateButton.textContent = "En Magasin";
      } else if (state === "OFFERED") {
        stateButton.textContent = "Proposé";
      } else if (state === "CONFIRMED") {
        stateButton.textContent = "Confirmé";
      } else if (state === "DENIED") {
        stateButton.textContent = "Refusé";
      } else if (state === "WORKSHOP") {
        stateButton.textContent = "Déposé en atelier";
      } else if (state === "SALE") {
        stateButton.textContent = "Mis en vente";
      } else if (state === "SOLD") {
        stateButton.textContent = "Vendu";
      } else if (state === "WITHDRAWN") {
        stateButton.textContent = "Retiré";
      }

      stateButton.classList.add('state-button');
      stateButtonsContainer.appendChild(stateButton);

      renderObjectsByStateBtn(stateButton);
    });

    main.appendChild(stateButtonsContainer);

    const dateTitle = document.createElement('h4');
    dateTitle.textContent = 'Recherche par date ';
    main.appendChild(dateTitle);
// create form for date search
    const form = document.createElement('form');
    form.classList.add('filterDate-form');
    form.id = "filterDate-form";

    const startDateLabel = document.createElement('label');
    startDateLabel.for = 'startDate';
    startDateLabel.innerText = 'Date de début : ';
    const startDateInput = document.createElement('input');
    startDateInput.type = 'date';
    startDateInput.id = 'startDate';

    const endDateLabel = document.createElement('label');
    endDateLabel.for = 'endDate';
    endDateLabel.innerText = 'Date de fin : ';
    const endDateInput = document.createElement('input');
    endDateInput.type = 'date';
    endDateInput.id = 'endDate';

    const submitButton = document.createElement('button');
    submitButton.type = 'submit';
    submitButton.innerText = 'Rechercher';

    form.append(startDateLabel, startDateInput, endDateLabel, endDateInput,
        submitButton);
    main.appendChild(form);

    form.addEventListener('submit', async (event) => {
      event.preventDefault();
      const startDate = startDateInput.value.split("-");
      const endDate = endDateInput.value.split("-");
      const objects = await getObjectsBetweenTwoDate(startDate, endDate);
      if (objects === undefined) {
        objectsContainer.innerHTML = "";
        objectsContainer.innerHTML = "Il n'y a pas d'objets pour ces dates.";
      } else {
        renderObjects(objects);
      }
    });

    // Create title for state buttons
    const numberObjects = document.createElement('h4');
    numberObjects.textContent = "";
    main.appendChild(numberObjects);

// Render all objects
    renderObjects(objectsList);

    // eslint-disable-next-line no-inner-declarations
    function renderObjects(objs) {
      // Clear current objects container
      objectsContainer.innerHTML = '';

      let compteur = 0; // Initialiser le compteur à 0
      // Render each object in the filtered list
      objs.forEach(object => {
        // eslint-disable-next-line no-plusplus
        compteur++;
        const objectElement = document.createElement('div');
        objectElement.classList.add('object');
        // Create object image element
        const objectImage = document.createElement('img');
        objectImage.src = `http://localhost:8080/objects/load/${object.id}`;
        objectImage.alt = object.name;
        objectImage.className = 'imgSearch';
        objectImage.classList.add('img-fluid');
        objectElement.appendChild(objectImage);

        // Create object name element
        const objectName = document.createElement('p');
        objectName.textContent = object.name;
        objectElement.appendChild(objectName);

        // Create object type element
        const objectType = document.createElement('p');
        const typeId = object.typeObject;
        const typeObject = typeObjectsList.find(type => type.id === typeId);
        objectType.textContent = `Type: ${typeObject.type}`;
        objectElement.appendChild(objectType);

        // Create object state element
        const objectState = document.createElement
        ('p');
        let state;
        if (object.state === "STORE") {
          state = "En Magasin";
        } else if (object.state === "OFFERED") {
          state = "Proposé";
        } else if (object.state === "CONFIRMED") {
          state = "Confirmé";
        } else if (object.state === "DENIED") {
          state = "Refusé";
        } else if (object.state === "WORKSHOP") {
          state = "Déposé en atelier";
        } else if (object.state === "SALE") {
          state = "Mis en vente";
        } else if (object.state === "SOLD") {
          state = "Vendu";
        } else if (object.state === "WITHDRAWN") {
          state = "Retiré";
        }
        objectState.textContent = `État: ${state}`;
        objectElement.appendChild(objectState);

        // Create object ID element
        const objectId = document.createElement('p');
        objectId.textContent = `ID: ${object.id}`;
        objectElement.appendChild(objectId);

        // Create object price element
        const price = document.createElement('p');
        if (object.sellingPrice === 0) {
          price.textContent = `- €`;
        } else {
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

      numberObjects.innerText = `Nombre d'objets trouvés : ${compteur}`;
    }

// Add some space between the input and the objects
    objectsContainer.style.marginTop = '10px';

// Render objects container
    main.appendChild(objectsContainer);
  } else {
    Navigate('/login');
  }
};

export default FilterPage;