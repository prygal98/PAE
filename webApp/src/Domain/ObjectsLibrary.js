import Navigate from "../Components/Router/Navigate";
import {renderPopUp} from "../utils/utilsForm";
import {clearActive} from "../utils/activeLink";
import {getAuthenticatedUser, getCurrentToken} from "../utils/auths";

async function getAllObjects() {
  let objects;
  try {
    const options = {
      method: 'GET', // *GET, POST, PUT, DELETE, etc.
      headers: {
        'Content-Type': 'application/json',
      },
    };

    const reponse = await fetch(
        `http://localhost:8080/objects/getObjectList`,
        options);

    if (!reponse.ok) {
      throw new Error(
          `fetch error : ${reponse.status}${reponse.statusText}`);
    }

    objects = await reponse.json();
  } catch (err) {
    console.error('error: ', err);
  }
  return objects;
} // fin function getAllObjects

async function getObjectbyId(id) {
  // Permet d'aller chercher les informations du produit
  let object;

  try {
    const options = {
      method: 'GET', // *GET, POST, PUT, DELETE, etc.
      headers: {
        'Content-Type': 'application/json',
      },
    };
    // eslint-disable-next-line prefer-template
    const reponse = await fetch(
        `http://localhost:8080/objects/getOneObject?id=${id}`, options);

    if (!reponse.ok) {
      throw new Error(
          `fetch error : ${reponse.status}${reponse.statusText}`);
    }
    object = await reponse.json();
  } catch (err) {
    // eslint-disable-next-line no-console
    console.error('error: ', err);
  }
  return object;
} // fin function getObjectbyId

async function acceptObject(id) {
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  const btn = document.getElementById('acceptbtn');
  btn.addEventListener("click", async (e) => {
    e.preventDefault();
    const newData = {
      id,
    }
    try {
      const options = {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify(newData),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token,
        },
      };
      // eslint-disable-next-line prefer-template
      const reponse = await fetch(`http://localhost:8080/objects/accept`,
          options);
      if (!reponse.ok) {
        const message = document.getElementById("message");
        message.innerHTML = ` <div id = "snackbar" > Une erreur est survenue ! </div>`;
        renderPopUp();
        throw new Error(
            // eslint-disable-next-line no-irregular-whitespace
            `fetch error : ${reponse.status} : ${reponse.statusText}`,
        );
      }
      Navigate('/object?id=', id);
    } catch (err) {
      // eslint-disable-next-line no-console
      console.error('error: ', err);
    }
  });
}

async function denyObject(id) {
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  const btn = document.getElementById('denybtn');
  btn.addEventListener("click", async (e) => {
    e.preventDefault();

    const reason = document.getElementById('refusalMessage').value;
    const newData = {
      id,
      refusalReason: reason,
    }
    try {
      const options = {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify(newData),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token,
        },
      };

      // eslint-disable-next-line prefer-template
      const reponse = await fetch(`http://localhost:8080/objects/deny`,
          options);
      if (!reponse.ok) {
        const message = document.getElementById("message");
        message.innerHTML = ` <div id = "snackbar" > Une erreur est survenue ! </div>`;
        await renderPopUp();
        setTimeout(() => {
          Navigate('/object?id=', id);
        }, 3000);
        throw new Error(
            // eslint-disable-next-line no-irregular-whitespace
            `fetch error : ${reponse.status} : ${reponse.statusText}`,
        );
      }
    } catch (err) {
      // eslint-disable-next-line no-console
      console.error('error: ', err);
    }
  });
}

async function workshopObject(id) {
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  const btn = document.getElementById('workshoped');
  btn.addEventListener("click", async (e) => {
    e.preventDefault();
    const newData = {
      id,
    }
    try {
      const options = {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify(newData),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token,
        },
      };
      // eslint-disable-next-line prefer-template
      const reponse = await fetch(
          `http://localhost:8080/objects/workshop`,
          options);

      if (!reponse.ok) {
        throw new Error(
            `fetch error : ${reponse.status}${reponse.statusText}`);
      }
      Navigate('/object?id=', id);
    } catch (err) {
      // eslint-disable-next-line no-console
      console.error('error: ', err);
    }
  });
}

async function storeObject(id) {
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  const btn = document.getElementById('stored');
  btn.addEventListener("click", async (e) => {
    e.preventDefault();
    const newData = {
      id,
    }
    try {
      const options = {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify(newData),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token,
        },
      };
      // eslint-disable-next-line prefer-template
      const reponse = await fetch(`http://localhost:8080/objects/store`,
          options);

      if (!reponse.ok) {
        throw new Error(
            `fetch error : ${reponse.status}${reponse.statusText}`);
      }
      Navigate('/object?id=', id);
    } catch (err) {
      // eslint-disable-next-line no-console
      console.error('error: ', err);
    }
  });
}

async function saleObject(id) {
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  const btn = document.getElementById('saled');
  btn.addEventListener("click", async (e) => {
    e.preventDefault();
    const sellingPrice = document.getElementById('sellingPric').value;
    if (sellingPrice <= 0) {
      const message = document.getElementById('message');
      message.innerHTML = `<div id="snackbar">Entrez un prix valide!</div>`;
      renderPopUp();
      return;
    }
    const newData = {
      id,
      sellingPrice
    }
    try {
      const options = {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify(newData),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token,
        },
      };
      // eslint-disable-next-line prefer-template
      const reponse = await fetch(`http://localhost:8080/objects/sale`,
          options);

      if (!reponse.ok) {
        throw new Error(
            `fetch error : ${reponse.status}${reponse.statusText}`);
      }
      Navigate('/object?id=', id);
    } catch (err) {
      // eslint-disable-next-line no-console
      console.error('error: ', err);
    }
  });
}

async function soldObject(id) {
  const btn = document.getElementById('soldes');
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  btn.addEventListener("click", async (e) => {
    e.preventDefault();
    const newData = {
      id,
    }
    try {
      const options = {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify(newData),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token,
        },
      };
      // eslint-disable-next-line prefer-template
      const reponse = await fetch(`http://localhost:8080/objects/sold`,
          options);
      if (!reponse.ok) {
        throw new Error(
            `fetch error : ${reponse.status}${reponse.statusText}`);
      }
      Navigate('/object?id=', id);
    } catch
        (err) {
      // eslint-disable-next-line no-console
      console.error('error: ', err);
    }
  });
}

async function withdrawObject(id) {
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  const btn = document.getElementById('withdraw');
  btn.addEventListener("click", async (e) => {
    e.preventDefault();
    const newData = {
      id,
    }
    try {
      const options = {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify(newData),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token,
        },
      };
      // eslint-disable-next-line prefer-template
      const reponse = await fetch(`http://localhost:8080/objects/withdraw`,
          options);

      if (!reponse.ok) {
        throw new Error(
            `fetch error : ${reponse.status}${reponse.statusText}`);
      }

      Navigate('/object?id=', id);
    } catch
        (err) {
      // eslint-disable-next-line no-console
      console.error('error: ', err);
    }
  });
}

async function objectSoldInStore(id) {
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  const btn = document.getElementById('soldInStore');
  btn.addEventListener("click", async (e) => {
    e.preventDefault();
    const sellingPrice = document.getElementById('sellingPrice').value;
    if (sellingPrice <= 0) {
      const message = document.getElementById('message');
      message.innerHTML = `<div id="snackbar">Entrez un prix valide!</div>`;
      renderPopUp();
      return;
    }
    const newData = {
      id,
      sellingPrice
    }
    try {
      const options = {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify(newData),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token,
        },
      };
      // eslint-disable-next-line prefer-template
      const reponse = await fetch(`http://localhost:8080/objects/soldInStore`,
          options);

      if (!reponse.ok) {
        throw new Error(
            `fetch error : ${reponse.status}${reponse.statusText}`);
      }
      Navigate('/object?id=', id);
    } catch (err) {
      // eslint-disable-next-line no-console
      console.error('error: ', err);
    }
  });
}

async function addObject(gsm, availability, name, type, description, photo) {
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  console.log('token', token);

  const message = document.getElementById('message');
  if (type.length === 0 || description.length === 0 || photo.length === 0
      || availability.length === 0 || gsm.length === 0) {

    message.innerHTML = `<div id="snackbar">Veuillez compléter le formulaire!</div>`;
    renderPopUp();
  }
  let newData;
  const User = await getAuthenticatedUser();

  if (User !== undefined) {
    newData = {
      name,
      description,
      type_object_id: type,
      availability_id: availability,
      phone_owner: gsm,
      ownerId: User.id
    };
  } else {
    newData = {
      name,
      description,
      type_object_id: type,
      availability_id: availability,
      phone_owner: gsm,
      ownerId: 0
    };
  }

  try {
    let options = {
      method: 'POST', // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify(newData),
      headers: {
        'Content-Type': 'application/json',
      },
    };

    const reponse = await fetch(
        'http://localhost:8080/objects/createObject',
        options);
    const objectId = await reponse.json();

    if (!reponse.ok) {
      message.innerHTML = `<div id="snackbar">L'objet n'a pas été ajouté!</div>`;
      renderPopUp();
      throw new Error(
          // eslint-disable-next-line no-irregular-whitespace
          `fetch error : ${reponse.status} : ${reponse.statusText}`,
      );
    }
    const picture = new FormData()
    picture.append("file", photo.files[0])
    options = {
      method: 'POST', // *GET, POST, PUT, DELETE, etc.
      body: picture,
      headers: {},
    };

    const reponsePicture = await fetch(
        `http://localhost:8080/objects/upload/${objectId.id}`, options);
    if (!reponsePicture.ok) {
      message.innerHTML = ` <div id = "snackbar" > L'objet n'a pas été ajouté! </div>`;
      renderPopUp();
      throw new Error(
          // eslint-disable-next-line no-irregular-whitespace
          `fetch error : ${reponsePicture.status} : ${reponsePicture.statusText}`,
      );
    }
    message.innerHTML = `<div id="snackbar">L'objet a bien été ajouté!</div>`;
    renderPopUp();
    clearActive();
    sessionStorage.clear();

    Navigate('/');

  } catch
      (err) {
    // eslint-disable-next-line
    console.error('error: ', err);
  }
}

async function getTypesObjet() {
  // Permet d'aller chercher les informations du produit
  let types;

  try {
    const options = {
      method: 'GET', // *GET, POST, PUT, DELETE, etc.
      headers: {
        'Content-Type': 'application/json',
      },
    };
    // eslint-disable-next-line prefer-template
    const reponse = await fetch(
        `http://localhost:8080/objects/getAllObjectsType`, options);

    if (!reponse.ok) {
      throw new Error(
          `fetch error : ${reponse.status}${reponse.statusText}`);
    }
    types = await reponse.json();
  } catch (err) {
    // eslint-disable-next-line no-console
    console.error('error: ', err);
  }
  return types;
}

async function getAllObjectsInStore() {
  let objects;
  try {
    const options = {
      method: 'GET', // *GET, POST, PUT, DELETE, etc.
      headers: {
        'Content-Type': 'application/json',
      },
    };

    const reponse = await fetch(
        `http://localhost:8080/objects/getAllObjectsInStoreOrSold`, options);

    if (!reponse.ok) {
      throw new Error(
          `fetch error : ${reponse.status}${reponse.statusText}`);
    }

    objects = await reponse.json();
  } catch (err) {
    console.error('error: ', err);
  }
  return objects;
} // fin function getAllObjects

async function getObjectsByType(type) {
  let objects;
  try {
    const options = {
      method: 'GET', // *GET, POST, PUT, DELETE, etc.
      headers: {
        'Content-Type': 'application/json',
      },
    };

    const reponse = await fetch(
        `http://localhost:8080/objects/getObjectsSortedByTypes?types=${type}`,
        options);

    if (!reponse.ok) {
      throw new Error(
          `fetch error : ${reponse.status}${reponse.statusText}`);
    }

    objects = await reponse.json();
  } catch (err) {
    console.error('error: ', err);
  }
  return objects;
}

async function getObjectsBetweenTwoDate(startDate, endDate) {
  let objects;
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;

  try {
    const startDateYear = startDate[0];
    const startDateMonths = startDate[1];
    const startDateDay = startDate[2];
    const endDateYear = endDate[0];
    const endDateMonths = endDate[1];
    const endDateDay = endDate[2];

    const newData = {
      startDate: `${startDateYear}-${startDateMonths}-${startDateDay}T00:00:00`,
      endDate: `${endDateYear}-${endDateMonths}-${endDateDay}T23:59:59`
    }

    const options = {
      method: 'POST', // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify(newData),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token,
      },
    };

    const reponse = await fetch(
        `http://localhost:8080/objects/getObjectsBetweenTwoDate`, options);

    if (!reponse.ok) {
      throw new Error(
          `fetch error : ${reponse.status}${reponse.statusText}`);
    }

    objects = await reponse.json();
  } catch (err) {
    console.error('error: ', err);
  }
  return objects;
}

async function getObjectsByUser(id) {
  let objects;
  try {
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      }
    };

    const response = await fetch(
        `http://localhost:8080/objects/getObjectsFromUser?id=${id}`, options);

    if (!response.ok) {
      throw new Error(`fetch error : ${response.status}${response.statusText}`);
    }

    objects = await response.json();
  } catch (err) {
    console.error('error: ', err);
  }
  return objects;
}

export {
  getAllObjects,
  getObjectbyId,
  acceptObject,
  denyObject,
  workshopObject,
  storeObject,
  saleObject,
  soldObject,
  addObject,
  getTypesObjet,
  getAllObjectsInStore,
  getObjectsByType,
  getObjectsByUser,
  getObjectsBetweenTwoDate,
  objectSoldInStore,
  withdrawObject
};