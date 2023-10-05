import HomePage from '../Pages/HomePage';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import ProfilePage from "../Pages/ProfilePage";
import ProposeObjectPage from "../Pages/ProposeObjectPage";
import ObjectPage from "../Pages/ObjectPage";
import FilterPage from "../Pages/FilterPage";
import DashboardPage from "../Pages/DashboardPage";
import ExternalUserPage from "../Pages/ExternalUserPage";
import MyObjectsPage from "../Pages/MyObjectsPage";
import UsersList from "../Pages/UsersList";

const routes = {
  '/': HomePage,
  '/login': LoginPage,
  '/register': RegisterPage,
  '/logout': '/logout',
  '/profile': ProfilePage,
  '/search': FilterPage,
  '/propose': ProposeObjectPage,
  '/object': ObjectPage,
  '/dashboard': DashboardPage,
  '/user': ExternalUserPage,
  '/myObjects':MyObjectsPage,
  '/users': UsersList
};

export default routes;
