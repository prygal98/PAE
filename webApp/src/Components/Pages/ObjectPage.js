/* eslint-disable no-console */
import 'bootstrap/dist/css/bootstrap.min.css';
import {clearPage} from '../../utils/render';
import {setActiveLink} from '../../utils/activeLink';
import {setUserIcon} from '../../utils/userIcon';
import Navbar from '../Navbar/Navbar';
import {getAuthenticatedUser, getCurrentToken} from "../../utils/auths";
import Navigate from "../Router/Navigate";
import {
  acceptObject,
  denyObject,
  getObjectbyId,
  getTypesObjet,
  objectSoldInStore,
  saleObject,
  soldObject,
  storeObject,
  withdrawObject,
  workshopObject
} from "../../Domain/ObjectsLibrary";
import {getUserById} from "../../Domain/UserLibrary";
import {renderPopUp} from "../../utils/utilsForm";

const ObjectPage = async () => {
      const listTypes = await getTypesObjet();
      const user = getAuthenticatedUser();
      if (user === undefined) {
        Navigate('/login');
      }
      console.log("listTypes : ", listTypes);
      clearPage();
      setActiveLink('userPage');
      setUserIcon('extUserPage');
      Navbar();

      const id = window.location.search;
      const url = id.split('=');
      const object = await getObjectbyId(url[1]);

      console.log('object : ', object);

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

      const html = `
        <div id="message">
              
        </div>
        <div class="row">
            <div class="col-md-3 border-right">
                <div class="d-flex flex-column align-items-center text-center p-3 py-5">
                    <div id="ObjectPhoto">
                        <img class="rounded-4" width="200px" src="http://localhost:8080/objects/load/${object.id}">
                    </div>             
                </div>
            </div> 
            <div class="col-md-5 border-right">
                <div class="p-3 py-5">
                    <div>
                        <h3>${object.name}</h3>               
                    </div>
                    <div>
                        <h4>${object.sellingPrice}€</h4>               
                    </div>
                    <div id="ownerDiv">
                                  
                    </div>
                    <div>
                        <p>${object.description}</p>               
                    </div>
                    <div>
                        <p>Etat: ${state}</p>               
                    </div>
                    <div>
                        <p>Type: ${getTypeById(object.typeObject)}</p>               
                    </div>
                    <div id="updateImg" >
                                      
                    </div>
                    <div id="updateDescAndType" style="display:none;" >
                       <button id="update-btn">Mettre à jour la description et le type</button>
                       <form id="update-form" style="display:none;">
                         <input type="text" id="description" placeholder="Description">
                         <div id="listeDeroulante"></div>
                         <button id="submit-btn">Mettre à jour</button>
                      </form>
                    </div>
                </div>
            </div>
            <div class="col-md-3 border-right">
                <div class="p-3 py-5">
                    <div id="onlyAdmin">
                                     
                    </div>
                </div>
            </div>
            <div id="stateChange">
                
                <div id="refusalReason">
                    
                </div>
            </div>
        </div>
    `

      function getTypeById(idType) {
        const typeObj = listTypes.find(type => type.id === idType);
        return typeObj ? typeObj.type : null;
      }

      const main = document.querySelector('main');
      main.innerHTML = html;

      const ownerDiv = document.getElementById('ownerDiv');

      if (object.ownerId === null || object.ownerId === 0) {
        ownerDiv.innerHTML = `<h5>Numero de gsm: ${object.phoneOwner}</h5>`;
      } else {
        const owner = await getUserById(+object.ownerId);
        console.log('le owner', owner);
        ownerDiv.innerHTML = `<h5>Proposé par <a href="#!" id="ownerBtn" name="${owner.id}">${owner.firstname} ${owner.lastname}</a></h5>`;

        const ownerBtn = document.getElementById('ownerBtn');
        ownerBtn.addEventListener('click', (e) => {
          e.preventDefault();
          Navigate('/user?=', ownerBtn.name);
        })
      }

      if (user.userRole === 'RESPONSIBLE' || user.userRole === 'HELPER') {
        const currentToken = getCurrentToken();
        const token = `Bearer ${currentToken}`;
        // RAPH
        const divUpdateImage = document.getElementById("updateImg");
        const uploadBtn = document.createElement("button");
        uploadBtn.textContent = "Mettre à jour la photo";

        const uploadForm = document.createElement("form");
        uploadForm.style.display = "none";
        uploadForm.addEventListener("submit", async (event) => {
          event.preventDefault();

          const photo = document.getElementById("photo");
          const objectId = new URLSearchParams(window.location.search).get("id");
          console.log("objectId : ", objectId);

          const picture = new FormData();
          picture.append("file", photo.files[0]);

          const options = {
            method: 'POST',
            body: picture,
            headers: {},
          };

          const reponsePicture = await fetch(
              `http://localhost:8080/objects/upload/${objectId}`, options
          );

          if (!reponsePicture.ok) {
            renderPopUp();
            throw new Error(
                `fetch error : ${reponsePicture.status} : ${reponsePicture.statusText}`
            );
          }

          // Mettre à jour la photo de l'objet sur la page
          divUpdateImage.src = URL.createObjectURL(photo.files[0]);
          Navigate('/object?id=', url[1]);
          // Masquer le formulaire
          uploadForm.style.display = "none";
        });

        const photoInput = document.createElement("input");
        photoInput.type = "file";
        photoInput.id = "photo";
        photoInput.name = "photo";

        const submitBtn = document.createElement("input");
        submitBtn.type = "submit";
        submitBtn.value = "Envoyer";

        uploadForm.appendChild(photoInput);
        uploadForm.appendChild(submitBtn);

        divUpdateImage.parentNode.insertBefore(uploadBtn,
            divUpdateImage.nextSibling);
        divUpdateImage.parentNode.insertBefore(uploadForm,
            divUpdateImage.nextSibling);

        uploadBtn.addEventListener("click", () => {
          uploadBtn.style.display = "none";
          uploadForm.style.display = "block";
        });

        // MOI
        const updateBtn2 = document.getElementById("update-btn");
        const updateForm2 = document.getElementById("update-form");
        const submitBtn2 = document.getElementById("submit-btn");

        const updateDescAndType = document.getElementById("updateDescAndType");
        updateDescAndType.style.display = "block";

        // Récupérer la div qui contiendra les listes déroulantes
        // Récupérer la liste déroulante
        const listeDeroulante = document.getElementById('listeDeroulante');

        // Parcourir la liste JSON pour créer les options
        const select = document.createElement("select");
        select.id = "select";
        /* eslint-disable no-plusplus */
        for (let i = 0; i < listTypes.length; i++) {
          // Créer un élément <option> pour chaque type
          const option = document.createElement('option');
          option.value = listTypes[i].id;
          option.text = listTypes[i].type;
          select.add(option);
          // Ajouter l'option à la liste déroulante
        }
        listeDeroulante.appendChild(select);

// Ajouter un événement de clic au bouton "Mettre à jour la description et le type"
        updateBtn2.addEventListener("click", async (event) => {
          // Empêcher le comportement par défaut du formulaire
          event.preventDefault();

          // Afficher le formulaire de modification
          updateForm2.style.display = "block";
        });

// Ajouter un événement de soumission au formulaire de modification
        submitBtn2.addEventListener("click", async (event) => {
          // Empêcher le comportement par défaut du formulaire
          event.preventDefault();

          // Récupérer la nouvelle description de l'objet
          const description = document.getElementById("description").value;

          // Récupérer le nouveau type de l'objet
          const type = document.getElementById("select").value;

          const newData = {
            id: object.id,
            description,
            type
          };

          const options = {
            method: 'POST', // *GET, POST, PUT, DELETE, etc.
            body: JSON.stringify(newData),
            headers: {
              'Content-Type': 'application/json',
              'Authorization': token,
            },
          };
          console.log("AA : ", object.id);
          console.log("options : ", options);
          console.log("newData : ", newData);

          const reponsePicture2 = await fetch(
              `http://localhost:8080/objects/updateObject`,
              options);
          console.log(await reponsePicture2);
          if (!reponsePicture2.ok) {
            renderPopUp();
            throw new Error(
                `fetch error : ${reponsePicture2.status} : ${reponsePicture2.statusText}`
            );
          } else {
            // Cacher le formulaire de modification
            updateForm2.style.display = "none";
            Navigate('/object?id=', url[1]);
          }
        });

        // FIN MOI
        if (object.state === 'OFFERED' && user.userRole === 'RESPONSIBLE') {
          const stateChangeForm = document.getElementById('stateChange');
          stateChangeForm.innerHTML += `
                   <div class="text-center pt-1 mb-3 pb-1">
                        <button class="btn btn-success rounded-pill " id="acceptbtn" type="button">Accepter</button>
                    </div>
                    <div class="text-center pt-1 mb-2 pb-1">
                        <button class="btn btn-danger rounded-pill" id="denie" type="button" >Refuser</button>                        
                    </div>
                  
            `;
          await acceptObject(url[1]);

          const denyUpdate = document.getElementById('denie');
          denyUpdate.addEventListener("click", async (e) => {
            e.preventDefault();
            const refusalReason = document.getElementById('refusalReason');
            refusalReason.innerHTML = `
                    <div>
                        <textarea class="form-control" id="refusalMessage" rows="3">
                        </textarea>
                        <div class="text-center pt-1 mb-2 pb-1">
                            <button class="btn btn-danger rounded-pill" id="denybtn" type="button" >Terminer</button>                        
                        </div>      
                    </div>
                `;
            await denyObject(url[1]);
          });

        } else if (object.state === 'OFFERED' && user.userRole !== 'RESPONSIBLE') {
          const stateChangeForm = document.getElementById('stateChange');
          stateChangeForm.textContent = ""
        } else if (object.state === 'SOLD') {
          const onlyadmin = document.getElementById('onlyAdmin');
          onlyadmin.innerHTML = `
                    <h3>Objet vendu!</h3>
            `;
        } else if (object.state === 'WORKSHOP') {
          const onlyadmin = document.getElementById('onlyAdmin');
          onlyadmin.innerHTML += `
                    <div class="text-center pt-1 mb-3 pb-1">
                        <button class="btn btn-dark rounded-pill " id="editObjet" type="button">Editer les informations</button>
                    </div>
                    <div class="text-center pt-1 mb-2 pb-1">
                        <button class="btn btn-outline-dark rounded-pill"  id="withdraw" type="button" >Retirer de la vente</button>                        
                    </div>           
            `;
          await withdrawObject(url[1]);

          const editObjet = document.getElementById('editObjet');
          editObjet.addEventListener("click", async (e) => {
            e.preventDefault();
            onlyadmin.innerHTML = `
                  
                    <div class="text-center pt-1 mb-2 pb-1">
                        <button class="btn btn-outline-dark rounded-pill" id="stored" type="button" >Indiquer: Déposé en magasin</button>                        
                    </div>  
                    
                `;

            await storeObject(url[1]);

          });
        } else if (object.state === 'STORE') {
          const onlyadmin = document.getElementById('onlyAdmin');
          onlyadmin.innerHTML += `
                    <div class="text-center pt-1 mb-3 pb-1">
                        <button class="btn btn-dark rounded-pill " id="editObjet"  >Editer les informations</button>
                    </div>
                    <div class="text-center pt-1 mb-2 pb-1">
                        <button class="btn btn-outline-dark rounded-pill" id="withdraw" type="button" >Retirer de la vente</button>                        
                    </div>           
            `;
          await withdrawObject(url[1]);

          const editObjet = document.getElementById('editObjet');
          editObjet.addEventListener("click", async (e) => {
            e.preventDefault();
            onlyadmin.innerHTML = `
                    <div class="text-center pt-1 mb-3 pb-1">
                        <button class="btn btn-outline-dark rounded-pill " id="workshoped" type="button" style="display: none;" >Indiquer: Déposé à l'atelier</button>
                    </div>
                    <div class="text-center pt-1 mb-2 pb-1">
                        <button class="btn btn-outline-dark rounded-pill" id="stored" type="button" style="display: none;" >Indiquer: Déposé en magasin</button>                        
                    </div>  
                    <div class="text-center pt-1 mb-2 pb-1">
                        <button class="btn btn-outline-dark rounded-pill" id="forsale" type="button" >Indiquer: Mis en vente</button> 
                        <div id="newPrice"></div>                       
                    </div>
                    <div class="text-center pt-1 mb-2 pb-1">
                        <button class="btn btn-outline-dark rounded-pill" id="wasSold" type="button" style="display: none" >Indiquer: à été vendu</button> 
                        <div id="newPriceSold"></div>                       
                    </div>  
                    
                `;
            const forsalebtn = document.getElementById('forsale');
            forsalebtn.addEventListener("click", async (d) => {
              d.preventDefault();
              const newPriceDiv = document.getElementById('newPrice');
              newPriceDiv.innerHTML = `
                    <div>
                        <label>Entrer un prix pour cet objet</label>
                        <input type="number" id="sellingPric">
                        <div class="text-center pt-1 mb-2 pb-1">
                            <button class="btn btn-danger rounded-pill" id="saled" type="button" >Terminer</button>                        
                        </div>      
                    </div>
                    `;
              await saleObject(url[1]);
            });

            const objectWasSold = document.getElementById('wasSold');
            if (user.userRole === 'RESPONSIBLE') {
              objectWasSold.style.display = 'block';
            }
            objectWasSold.addEventListener("click", async (d) => {
              d.preventDefault();
              const newPriceDiv = document.getElementById('newPriceSold');
              newPriceDiv.innerHTML = `
                    <div>
                        <label>Entrer le prix auxquel l'objet à été vendu</label>
                        <input type="number" id="sellingPrice">
                        <div class="text-center pt-1 mb-2 pb-1">
                            <button class="btn btn-danger rounded-pill" id="soldInStore" type="button" >Terminer</button>                        
                        </div>      
                    </div>
                    `;
              await objectSoldInStore(url[1]);
            });
            await workshopObject(url[1]);
            await storeObject(url[1]);

          });
        } else if (object.state === 'SALE') {
          const onlyadmin = document.getElementById('onlyAdmin');

          onlyadmin.innerHTML += `
                    <div class="text-center pt-1 mb-3 pb-1">
                        <button class="btn btn-dark rounded-pill " id="editObjet" type="button">Editer les informations</button>
                    </div>
                    <div class="text-center pt-1 mb-2 pb-1">
                        <button class="btn btn-outline-dark rounded-pill" id="withdraw" type="button" >Retirer de la vente</button>                        
                    </div>           
       
            `;
          await withdrawObject(url[1]);
          const editObjet = document.getElementById('editObjet');
          editObjet.addEventListener("click", async (e) => {
            e.preventDefault();
            onlyadmin.innerHTML = `
               <div class="text-center pt-1 mb-2 pb-1">
                  <button class="btn btn-outline-dark rounded-pill" id="soldes" type="button">Indiquer: Vendu</button>  
               </div> 
              
              `;

            await soldObject(url[1]);
          });

        } else if (object.state === 'WITHDRAWN') {
          const onlyadmin = document.getElementById('onlyAdmin');
          onlyadmin.innerHTML = `
                    <h3>Objet retiré de la vente</h3>
            `;
        } else if (object.state !== 'DENIED') {
          const onlyadmin = document.getElementById('onlyAdmin');
          onlyadmin.innerHTML += `
                    <div class="text-center pt-1 mb-3 pb-1">
                        <button class="btn btn-dark rounded-pill " id="editObjet" type="button">Editer les informations</button>
                    </div>
                    <div class="text-center pt-1 mb-2 pb-1">
                        <button class="btn btn-outline-dark rounded-pill"  id="withdraw" type="button" >Retirer de la vente</button>                        
                    </div>           
            `;
          await withdrawObject(url[1]);

          const editObjet = document.getElementById('editObjet');
          editObjet.addEventListener("click", async (e) => {
            e.preventDefault();
            onlyadmin.innerHTML = `
                    <div class="text-center pt-1 mb-3 pb-1">
                        <button class="btn btn-outline-dark rounded-pill " id="workshoped" type="button">Indiquer: Deposé à l'atelier</button>
                    </div>
                    <div class="text-center pt-1 mb-2 pb-1">
                        <button class="btn btn-outline-dark rounded-pill" id="stored" type="button" >Indiquer: Deposé en magasin</button>                        
                    </div>  
                    
                `;

            await workshopObject(url[1]);
            await storeObject(url[1]);

          });
        } else if (object.state === 'DENIED') {
          const onlyadmin = document.getElementById('onlyAdmin');
          onlyadmin.innerHTML = `
                    <h3>Objet refusé</h3>
            `;
        }
      }

    }
;

export default ObjectPage;