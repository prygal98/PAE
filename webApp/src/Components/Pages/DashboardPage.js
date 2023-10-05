import {setActiveLink} from '../../utils/activeLink';
import {setUserIcon} from '../../utils/userIcon';
import Navbar from '../Navbar/Navbar';
import {getAuthenticatedUser} from '../../utils/auths';
import Navigate from '../Router/Navigate';
import {
  getAllObjects,
  getObjectsBetweenTwoDate
} from '../../Domain/ObjectsLibrary';
import {clearPage} from "../../utils/render";

const DashboardPage = async () => {
  const user = await getAuthenticatedUser();
  if (user.userRole === 'RESPONSIBLE' || user.userRole === 'HELPER') {
    setActiveLink('userPage');
    setUserIcon('extUserPage');
    Navbar();

    const main = document.querySelector('main');
    clearPage();
    const titre = document.createElement("h2");
    titre.id = "DashBoard-H2"
    titre.textContent = "Choisissez une date pour filtrer votre recherche"
    const dashboardContainer = document.createElement('div');
    dashboardContainer.classList.add('dashboard-container');
    dashboardContainer.id = "dashboard-container";

    const form = document.createElement('form');
    form.classList.add('dashboard-form');
    form.id = "dashboard-form";

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

    const allObjects = await getAllObjects();
    main.appendChild(titre);
    main.appendChild(form);

    const objectsContainer = document.createElement('div');
    objectsContainer.classList.add('objects-container');
    objectsContainer.id = "objects-container";

    const table = document.createElement("table");
    table.id = "table";
    const headerRow = table.insertRow();
    const stateHeader = headerRow.insertCell();
    stateHeader.innerHTML = "État";
    const countHeader = headerRow.insertCell();
    countHeader.innerHTML = "Nombres";

    const stateCounts = {};

    allObjects.forEach((obj) => {
      const {state} = obj;
      if (stateCounts[state] === undefined) {
        stateCounts[state] = 1;
      } else {
        // eslint-disable-next-line no-plusplus
        stateCounts[state]++;
      }
    });

    const states = [
      "OFFERED",
      "CONFIRMED",
      "DENIED",
      "WORKSHOP",
      "STORE",
      "SALE",
      "SOLD",
      "WITHDRAWN",
    ];

    states.forEach((state) => {
      const row = table.insertRow();
      const stateCell = row.insertCell();
      let state1;
      if (state === "STORE") {
        state1 = "Mis en magasin";
      } else if (state === "OFFERED") {
        state1 = "Proposé";
      } else if (state === "CONFIRMED") {
        state1 = "Confirmé";
      } else if (state === "DENIED") {
        state1 = "Refusé";
      } else if (state === "WORKSHOP") {
        state1 = "Déposé en atelier";
      } else if (state === "SALE") {
        state1 = "En vente";
      } else if (state === "SOLD") {
        state1 = "Vendu";
      } else if (state === "WITHDRAWN") {
        state1 = "Retiré";
      }
      stateCell.innerHTML = state1;
      const countCell = row.insertCell();
      countCell.innerHTML = stateCounts[state] || 0;
    });

    const totalObjects = allObjects.length;
    const totalObjectsText = document.createElement("p");
    totalObjectsText.id = "totalObjectsText";
    totalObjectsText.innerHTML = `Nombre total d'objets :  ${totalObjects}`;

    dashboardContainer.append(form, totalObjectsText, table);
    main.appendChild(dashboardContainer);

    form.addEventListener('submit', async (event) => {
      event.preventDefault();
      table.innerHTML = "";
      const startDate = startDateInput.value.split("-");
      const endDate = endDateInput.value.split("-");

      const objects = await getObjectsBetweenTwoDate(startDate, endDate);
      if (objects === undefined) {
        totalObjectsText.innerHTML = "";
        totalObjectsText.innerHTML = `Aucun résultat n'a été trouvé pour les dates sélectionnées`;

      } else {
        // eslint-disable-next-line no-shadow
        const stateCounts = {};

        objects.forEach((obj) => {
          const {state} = obj;
          if (stateCounts[state] === undefined) {
            stateCounts[state] = 1;
          } else {
            // eslint-disable-next-line no-plusplus
            stateCounts[state]++;
          }
        });

        states.forEach((state) => {
          const row = table.insertRow();
          const stateCell = row.insertCell();
          let state1;
          if (state === "STORE") {
            state1 = "Mis en magasin";
          } else if (state === "OFFERED") {
            state1 = "Proposé";
          } else if (state === "CONFIRMED") {
            state1 = "Confirmé";
          } else if (state === "DENIED") {
            state1 = "Refusé";
          } else if (state === "WORKSHOP") {
            state1 = "Déposé en atelier";
          } else if (state === "SALE") {
            state1 = "En vente";
          } else if (state === "SOLD") {
            state1 = "Vendu";
          } else if (state === "WITHDRAWN") {
            state1 = "Retiré";
          }
          stateCell.innerHTML = state1;
          const countCell = row.insertCell();
          countCell.innerHTML = stateCounts[state] || 0;
        });

        // eslint-disable-next-line no-shadow
        const totalObjects = objects.length;
        // eslint-disable-next-line no-shadow
        const totalObjectsText = document.createElement("p");
        totalObjectsText.innerHTML = `Nombre total d'objets: ${totalObjects}`;

        dashboardContainer.replaceChild(table, dashboardContainer.lastChild);
        dashboardContainer.replaceChild(totalObjectsText,
            dashboardContainer.firstChild.nextSibling);
      }

    });
  } else {
    Navigate('/login');
  }
};

export default DashboardPage;