import 'bootstrap/dist/css/bootstrap.min.css';
import { clearPage } from '../../utils/render';
import { setUserIcon } from '../../utils/userIcon';
import Navbar from '../Navbar/Navbar';
import { getAuthenticatedUser , getCurrentToken} from '../../utils/auths';
import Navigate from '../Router/Navigate';
import { getUserById } from '../../Domain/UserLibrary';
import { getObjectsByUser } from '../../Domain/ObjectsLibrary';
import {renderPopUp} from "../../utils/utilsForm";

const ExternalUserPage = async () => {
    const me = getAuthenticatedUser();
    const token = getCurrentToken();
    if (me === undefined) {
        Navigate('/login');
    }
    const id = window.location.search;
    const url = id.split('=');
    const user = await getUserById(url[1]);

    clearPage();
    setUserIcon('extUserPage');
    Navbar();
    const main = document.querySelector('main');


    // Add title for user presentation
    const userTitle = document.createElement('h2');
    userTitle.innerText = 'Profil de l\'utilisateur';

    main.appendChild(userTitle);

    const divRow = document.createElement('div');
    divRow.classList.add('row');
    const divCol = document.createElement('div');
    divCol.classList.add('col-md-3', 'col-lg-2', 'border-right');
    const divFlex = document.createElement('div');
    divFlex.classList.add('d-flex', 'flex-column', 'align-items-center',
        'text-center', 'p-3', 'py-5');
    const img = document.createElement('img');
    img.classList.add('rounded-circle', 'mt-3');
    img.setAttribute('width', '150px');
    img.setAttribute('src', `http://localhost:8080/users/load/${user.id}`);
    const divUserInfo = document.createElement('div');
    divUserInfo.setAttribute('id', 'userInfo');
    const spanBold = document.createElement('span');
    spanBold.classList.add('font-weight-bold');
    spanBold.textContent = `${user.firstname} ${user.lastname}`;
    const br = document.createElement('br');
    const spanBlack = document.createElement('span');
    spanBlack.classList.add('text-black-50');
    spanBlack.textContent = user.email;
    divUserInfo.appendChild(spanBold);
    divUserInfo.appendChild(br);
    divUserInfo.appendChild(spanBlack);
    const indiquerAidant = document.createElement('div');
    const indiquerResponsable = document.createElement('div');
    const html1 = `<div class="text-center pt-1 mb-3 pb-1" id="level">
              <div id="displayUsers">
              <button class="btn btn-success rounded-pill" id="makeHelper" type="button">Indiquer comme aidant</button>
               <div id="message"></div> 
              </div>
            </div>
`;
    const html2 = `<div class="text-center pt-1 mb-3 pb-1" id="level">
              <div id="displayUsers">
              <button class="btn btn-success rounded-pill" id="makeResponsible" type="button">Indiquer comme responsable</button>
               <div id="message"></div> 
              </div>
            </div>
`;
    indiquerAidant.innerHTML="";
    indiquerResponsable.innerHTML="";
    // Add GSM information
    const divGSM = document.createElement('div');
    const pLabelGSM = document.createElement('p');
    pLabelGSM.textContent = 'GSM : ';
    const pGSM = document.createElement('p');
    pGSM.textContent = user.gsm;
    divGSM.appendChild(pLabelGSM);
    divGSM.appendChild(pGSM);
    divFlex.appendChild(img);
    divFlex.appendChild(divUserInfo);
    divFlex.appendChild(divGSM);
    divFlex.appendChild(indiquerAidant);
    divFlex.appendChild(indiquerResponsable);
    divCol.appendChild(divFlex);
    divRow.appendChild(divCol);
    const divUserObjects = document.createElement('div');
    divUserObjects.setAttribute('id', 'userObjects');



    divRow.appendChild(divUserObjects);
    main.appendChild(divRow);

    if (me.userRole === 'RESPONSIBLE' || me.userRole === 'HELPER') {

        if (me.userRole === 'RESPONSIBLE'){
            // Add title for user object list
            indiquerAidant.innerHTML=html1;
            const userToHelper = document.getElementById("makeHelper");
            userToHelper.addEventListener('click', async (e) => {
                e.preventDefault();
                const id2=url[1];

                // eslint-disable-next-line prefer-template
                const newData = {
                    idHelper: id2,
                }
                try {
                    const options = {
                        method: 'POST', // *GET, POST, PUT, DELETE, etc.
                        body: JSON.stringify(newData),
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${token}`,
                        },
                    };
                    // eslint-disable-next-line prefer-template
                    const reponse = await fetch(`http://localhost:8080/users/makeHelper`,
                        options);

                    const message = document.getElementById('message');
                    message.innerHTML = `<div id="snackbar">Vous avez confirmé l'inscription d'un aidant</div>`;
                    renderPopUp();

                    if (!reponse.ok) {
                        throw new Error(`fetch error : ${reponse.status}${reponse.statusText}`);
                    }
                    userToHelper.style.display = 'none';
                    // Attendre 2 secondes avant la redirection
                    setTimeout(() => {
                        Navigate('/user?=', url[1]);
                    }, 3000);
                } catch (err) {
                    // eslint-disable-next-line no-console
                    console.error('error: ', err);
                }
            });

            indiquerResponsable.innerHTML=html2;
            const userToResponsible = document.getElementById("makeResponsible");
            userToResponsible.addEventListener('click', async (e) => {
                e.preventDefault();
                const id2=url[1];

                // eslint-disable-next-line prefer-template
                const newData = {
                    idHelper: id2,
                }
                try {
                    const options = {
                        method: 'POST', // *GET, POST, PUT, DELETE, etc.
                        body: JSON.stringify(newData),
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${token}`,
                        },
                    };
                    // eslint-disable-next-line prefer-template
                    const reponse = await fetch(`http://localhost:8080/users/makeResponsible`,
                        options);

                    const message = document.getElementById('message');
                    message.innerHTML = `<div id="snackbar">Vous avez confirmé l'inscription d'un responsable</div>`;
                    renderPopUp();

                    if (!reponse.ok) {
                        throw new Error(`fetch error : ${reponse.status}${reponse.statusText}`);
                    }
                    // Attendre 2 secondes avant la redirection
                    setTimeout(() => {
                        Navigate('/user?=', url[1]);
                    }, 3000);
                } catch (err) {
                    // eslint-disable-next-line no-console
                    console.error('error: ', err);
                }
            });
        }


        divRow.appendChild(divUserObjects);
        main.appendChild(divRow);

        const getObjectOfUserById = await getObjectsByUser(url[1]);



        if (getObjectOfUserById ===  undefined){
            const objectTitle = document.createElement('h2');
            objectTitle.innerText = 'l\'utilisateur n\'a pas encore ajouté d\'objets';
            divUserObjects.appendChild(objectTitle);

            divRow.appendChild(divUserObjects);
            main.appendChild(divRow);
        }else {

            const objectTitle = document.createElement('h2');
            objectTitle.innerText = 'Liste des objets de l\'utilisateur';
            divUserObjects.appendChild(objectTitle);

            divRow.appendChild(divUserObjects);
            main.appendChild(divRow);
        }
        // Add title for user object list



        const objectsContainer = document.createElement('div');
        objectsContainer.classList.add('d-flex', 'flex-wrap',
            'justify-content-center', 'align-items-center', 'my-3');

        getObjectOfUserById.forEach((object) => {
            const objectElement = document.createElement('div');
            objectElement.classList.add('object', 'm-3', 'p-3', 'text-center',
                'bg-light', 'border', 'rounded');

            // Create object image element
            const objectImage = document.createElement('img');
            objectImage.src = `http://localhost:8080/objects/load/${object.id}`;
            objectImage.alt = object.name;
            objectImage.classList.add('img-fluid', 'rounded');
            objectElement.appendChild(objectImage);

            // Create object name element
            const objectName = document.createElement('h5');
            objectName.textContent = object.name;
            objectName.classList.add('mt-2', 'mb-0');
            objectElement.appendChild(objectName);

            // Create object description element
            const description = document.createElement('p');
            description.textContent = `description : ${object.description}`;
            description.classList.add('text-secondary', 'my-1');
            objectElement.appendChild(description);

            // Create object price element
            const price = document.createElement('h6');
            if (object.sellingPrice === 0){
                price.textContent = `- €`;
            }else {
                price.textContent = `Prix : ${object.sellingPrice} €`;
            }
            price.classList.add('text-success', 'mb-0');
            objectElement.appendChild(price);

            // Add click event listener to object element
            objectElement.addEventListener('click', () => {
                // Navigate to object page using object's ID
                Navigate(`/object?id=${object.id}`);
            });

            // Add object to container
            objectsContainer.appendChild(objectElement);
        });

        divUserObjects.appendChild(objectsContainer);
    }

// If user is not a helper or responsible, display a message informing that the user does not have any objects
    if (me.userRole !== 'RESPONSIBLE' && me.userRole !== 'HELPER') {
        const noObjectsMessage = document.createElement('p');
        noObjectsMessage.classList.add('my-3');
        noObjectsMessage.textContent = 'Cet utilisateur n\'a pas encore ajouté d\'objets.';
        divUserObjects.appendChild(noObjectsMessage);
    }
}
export default ExternalUserPage;