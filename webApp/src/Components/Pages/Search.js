/* eslint-disable no-console */
import 'bootstrap/dist/css/bootstrap.min.css';
import { clearPage } from '../../utils/render';
import {setActiveLink } from '../../utils/activeLink';
import { setUserIcon } from '../../utils/userIcon';
import Navbar from '../Navbar/Navbar';
import {getAuthenticatedUser} from "../../utils/auths";
import Navigate from "../Router/Navigate";


const html = `
<div id="message">

</div>
<div class="row d-flex justify-content-center align-items-center h-100">
    <div class="col-xl-10">
        <div class="card rounded-3 text-black" id="backseparator">
            <div class="row g-0">

                <div class="">

                    <div class="p-md-5 mx-md-4">
                        <div class="text-center">
                            <h1>Choisissez vos filtres de recherche!</h1>
                        </div>
                        <div class="d-flex align-items-center justify-content-center pb-4">
                            <p class="mb-0 me-2">Ceux-ci ne sont pas obligatoires</p>
                        </div>

                        <div class="form-outline mb-4">
                            <label class="form-label" for="motcles">Recherche par mot clés</label>
                            <input type="text" id="motcles" class="form-control"
                                   placeholder="Entrez un mot clé..." />
                        </div>

                        <div class="form-outline mb-4">
                            <label class="form-label" for="types">Recherche par type</label>
                            <input type="text" id="types" class="form-control"
                                   placeholder="Entrez un type d'objet..." />
                        </div>
                        <div class="form-outline mb-4">
                            <label class="form-label" for="de">De</label>
                            <input type="number" id="de" class="form-control" placeholder="Entrez une somme.." />
                            <label class="form-label" for="de">à</label>
                            <input type="number" id="a" class="form-control" placeholder="Entrez une somme..." />
                        </div>

                        <div class="text-center pt-1 mb-5 pb-1">
                            <button class="btn btn-success rounded-pill"
                                    id="rechercher" type="button" style="color : white">Rechercher</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
`;


const SearchPage = () => {
    const user = getAuthenticatedUser();
    if (user === undefined) {
        Navigate('/login');
    }
    clearPage();
    setActiveLink('searchPage');
    setUserIcon('extUserPage');
    Navbar();

    const main = document.querySelector('main');
    main.innerHTML = html;
};

export default SearchPage;