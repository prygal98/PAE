/* eslint-disable class-methods-use-this */
import Navigate from '../Components/Router/Navigate';
import {clearActive} from '../utils/activeLink';
import {renderPopUp} from '../utils/utilsForm';
import Navbar from '../Components/Navbar/Navbar';
import {getCurrentToken, setAuthenticatedUser} from "../utils/auths";
import Footer from "../Components/Footer/Footer";

// import {setUserPFP, uploadUserPFP} from "../utils/uploadFileUtils";

async function login() {

  const btn = document.getElementById('login');

  btn.addEventListener('click', async (e) => {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('pwd').value;

    if (email.length === 0 || password.length === 0) {
      const message = document.getElementById('message');
      message.innerHTML = `<div id="snackbar">Veuillez compléter le formulaire!</div>`;
      renderPopUp();
    }

    const newData = {
      email,
      password,
    };

    try {
      const options = {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify(newData),
        headers: {
          'Content-Type': 'application/json',
        },
      };

      const reponse = await fetch(`http://localhost:8080/users/login`, options);
      if (!reponse.ok) {

        const message = document.getElementById('message');
        message.innerHTML = `<div id="snackbar">E-mail ou mot de passe incorrect!</div>`;
        renderPopUp();

        throw new Error(
            // eslint-disable-next-line no-irregular-whitespace
            `fetch error : ${reponse.status} : ${reponse.statusText}`,
        );
      }

      const user = await reponse.json();
      // sets the Authenticated user to the actual user
      setAuthenticatedUser(user);
      // reloads Navbar (display is different when user logged in)
      Navbar();
      Footer();
      // navigte to homePage
      clearActive();

      Navigate('/');

    } catch (err) {
      // eslint-disable-next-line
      console.error('error: ', err);
    }
  });
} // end login

async function register() {

  const btn = document.getElementById('register');

  btn.addEventListener('click', async (e) => {
    e.preventDefault();
    const pfp = document.getElementById('pfp');
    const firstname = document.getElementById('firstname').value;
    const lastname = document.getElementById('lastname').value;
    const email = document.getElementById('email').value;
    const gsm = document.getElementById('GSM').value;
    const password = document.getElementById('pwd').value;
    const passwordConfirmed = document.getElementById('pwdConf').value;
    const message = document.getElementById('message');

    if (
        lastname.length === 0 ||
        firstname.length === 0 ||
        email.length === 0 ||
        gsm.length === 0 ||
        password.length === 0 ||
        passwordConfirmed.length === 0
    ) {
      message.innerHTML = `<div id="snackbar">Veuillez compléter le formulaire!</div>`;
      renderPopUp();

    } else if (password !== passwordConfirmed) {
      message.innerHTML = `<div id="snackbar">Les mots de passe ne correspondent pas!</div>`;
      renderPopUp();
    } else {
      const newData = {
        email,
        password,
        firstname,
        lastname,
        gsm,
      };

      try {
        let options = {
          method: 'POST', // *GET, POST, PUT, DELETE, etc.
          body: JSON.stringify(newData),
          headers: {
            'Content-Type': 'application/json',
          },
        };

        const reponse = await fetch(`http://localhost:8080/users/register`,
            options);
        if (!reponse.ok) {
          // recup token renvoyer en reponse
          message.innerHTML = `<div id="snackbar">Erreur lors de l'inscription!</div>`;
          renderPopUp();
          throw new Error(
              // eslint-disable-next-line no-irregular-whitespace
              `fetch error : ${reponse.status} : ${reponse.statusText}`,
          );
        }

        const user = await reponse.json();
        setAuthenticatedUser(user);
        const currentToken = getCurrentToken();
        const token = `Bearer ${currentToken}`;

        const picture = new FormData()
        picture.append("file", pfp.files[0])
        options = {
          method: 'POST', // *GET, POST, PUT, DELETE, etc.
          body: picture,
          headers: {
            'Authorization': token,
          },
        };
        const reponsePicture = await fetch(`http://localhost:8080/users/upload`,
            options);
        if (!reponsePicture.ok) {
          message.innerHTML = `<div id="snackbar">Erreur lors de l'inscription!</div>`;
          renderPopUp();
          throw new Error(
              // eslint-disable-next-line no-irregular-whitespace
              `fetch error : ${reponsePicture.status} : ${reponsePicture.statusText}`,
          );
        }

        clearActive();
        /*
        setTimeout(() => {
        }, 3000);

         */
        Navigate('/login');
      } catch (err) {
        // eslint-disable-next-line
        console.error('error: ', err);
      }
    }

  });
} // end register

async function saveUserInfo() {
  const btn = document.getElementById('saveUserInf');
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  await btn.addEventListener('click', async (e) => {
    e.preventDefault();
    const firstname = document.getElementById('newFirstname').value;
    const lastname = document.getElementById('newLastname').value;
    const email = document.getElementById('newEmail').value;
    const gsm = document.getElementById('newGsm').value;
    const oldPassword = document.getElementById('oldPassword').value;
    const oldPassword2 = document.getElementById('oldPassword2').value;
    const newPassword = document.getElementById('newPassword').value;

    if (oldPassword !== oldPassword2) {

      const message = await document.getElementById('message');
      message.innerHTML = `<div id="snackbar">Les 2 mots de passes ne 
          correspondent pas !</div>`;
      renderPopUp();
      throw new Error(
          `Error les 2 mots de passes ne sont pas les mêmes`
      );
    }

    let newData;
    if (newPassword === undefined) {
      newData = {
        firstname,
        lastname,
        email,
        password: oldPassword,
        gsm
      };
    } else {
      newData = {
        firstname,
        lastname,
        email,
        password: oldPassword,
        newPassword,
        gsm
      };
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

      const reponse = await fetch(`http://localhost:8080/users/updateUser`,
          options);
      if (!reponse.ok) {
        const message = await document.getElementById('message');
        message.innerHTML = `<div id="snackbar">Le mot de passe que vous avez 
            entré n'est pas correct !</div>`;
        renderPopUp();
        throw new Error(
            // eslint-disable-next-line no-irregular-whitespace
            `fetch error : ${reponse.status} : ${reponse.statusText}`,
        );
      } else {
        const message = await document.getElementById('message');
        message.innerHTML = `<div id="snackbar">Les modifications ont 
été faites !</div>`;
        renderPopUp();
        const user = await reponse.json();
        setAuthenticatedUser(user);
        Navigate('/profile');

      }
    } catch (err) {
      // eslint-disable-next-line
      console.error('error: ', err);
    }
  });
}

async function updateUserPwdFetch() {
  const btn = document.getElementById('saveUserPwd');

  btn.addEventListener('click', async (e) => {
    e.preventDefault();
    const firstname = document.getElementById('firstname').value;
    const lastname = document.getElementById('lastname').value;
    const email = document.getElementById('email').value;

    const newData = {
      firstname,
      lastname,
      email
    };

    try {
      const options = {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify(newData),
        headers: {
          'Content-Type': 'application/json',

        },
      };

      const reponse = await fetch(`http://localhost:8080/users/update`,
          options);
      if (!reponse.ok) {
        throw new Error(
            // eslint-disable-next-line no-irregular-whitespace
            `fetch error : ${reponse.status}:${reponse.statusText}`,
        );
      }

      Navigate('/profile');

    } catch (err) {
      // eslint-disable-next-line
      console.error('error: ', err);
    }
  });
}

async function getAllUsers() {
  let users;
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  try {
    const options = {
      method: 'GET', // *GET, POST, PUT, DELETE, etc.
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token,
      },
    };

    const reponse = await fetch(`http://localhost:8080/users/listUsers`,
        options);

    if (!reponse.ok) {
      throw new Error(`fetch error : ${reponse.status}${reponse.statusText}`);
    }

    users = await reponse.json();
  } catch (err) {
    console.error('error: ', err);
  }
  return users;
} // fin function getAllUsers

async function makeUserHelper(id) {
  const btn = document.getElementById('makeHelper');
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  btn.addEventListener("click", async (e) => {
    e.preventDefault();
    const newData = {
      idHelper: id,
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
      const reponse = await fetch(`http://localhost:8080/users/makeHelper`,
          options);

      if (!reponse.ok) {
        throw new Error(`fetch error : ${reponse.status}${reponse.statusText}`);
      }
      Navigate('/users');
    } catch (err) {
      // eslint-disable-next-line no-console
      console.error('error: ', err);
    }
  });
}

async function getUserById(id) {
  let user;
  try {
    const options = {
      method: 'GET', // *GET, POST, PUT, DELETE, etc.
      headers: {
        'Content-Type': 'application/json',
      },
    };

    const reponse = await fetch(`http://localhost:8080/users/getUser/${id}`,
        options);

    if (!reponse.ok) {
      throw new Error(`fetch error : ${reponse.status}${reponse.statusText}`);
    }

    user = await reponse.json();
  } catch (err) {
    console.error('error: ', err);
  }
  return user;
}

export {
  login,
  register,
  updateUserPwdFetch,
  saveUserInfo,
  getAllUsers,
  makeUserHelper,
  getUserById,
};