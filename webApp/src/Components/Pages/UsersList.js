import { clearPage } from '../../utils/render';
import { setActiveLink } from '../../utils/activeLink';
import { setUserIcon } from '../../utils/userIcon';
import Navbar from '../Navbar/Navbar';
import { getAuthenticatedUser } from '../../utils/auths';
import Navigate from '../Router/Navigate';
import { getAllUsers } from '../../Domain/UserLibrary';


const UserList = async () => {
  const user = getAuthenticatedUser();
  if (user.userRole === 'RESPONSIBLE' || user.userRole === 'HELPER') {

    setActiveLink('userPage');
    setUserIcon('extUserPage');
    Navbar();

    const usersList = await getAllUsers();
    const main = document.querySelector('main');
    clearPage();
    const objectsContainer = document.createElement('div');
    objectsContainer.classList.add('objects-container');

    const pageTitle = document.createElement('h2');
    pageTitle.textContent = 'Liste des utilisateurs';
    main.appendChild(pageTitle);

    renderUsers(usersList);

    // eslint-disable-next-line no-inner-declarations
    function renderUsers(users) {
      objectsContainer.innerHTML = '';

      users.forEach(us=> {
        const userElement = document.createElement('div');
        userElement.classList.add('user');

        const objectElement = document.createElement('div');
        objectElement.classList.add('object');

        // Create object image element
        const objectImage = document.createElement('img');
        objectImage.src = `http://localhost:8080/users/load/${us.id}`;
        objectImage.alt = us.id;
        objectImage.className = 'imgSearch';
        objectImage.classList.add('img-fluid');
        userElement.appendChild(objectImage);



        // Create user name element
        const userName = document.createElement('p');
        userName.textContent = `${us.firstname} ${us.lastname}`;
        userElement.appendChild(userName);

        // Create user email element
        const userEmail = document.createElement('p');
        userEmail.textContent = `Email: ${us.email}`;
        userElement.appendChild(userEmail);

        // Create user gsm element
        const userGsm = document.createElement('p');
        userGsm.textContent = `GSM: ${us.gsm}`;
        userElement.appendChild(userGsm);

        // Create user role element
        const userRole = document.createElement('p');
        userRole.textContent = `Rôle: ${us.userRole}`;
        userElement.appendChild(userRole);

        // Add click event listener to user element
        userElement.addEventListener('click', () => {
          // Navigate to user page using user's ID
          Navigate(`/user?id=${us.id}`);
        });

        // Add user to container
        objectsContainer.appendChild(userElement);
      });
    }

    objectsContainer.style.marginTop = '10px';

    main.appendChild(objectsContainer);
  } else {
    Navigate('/login');
  }
};

export default UserList;
