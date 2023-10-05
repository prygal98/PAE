import { getAuthenticatedUser,} from '../../utils/auths';
import {clearPage } from '../../utils/render';
import Navbar from '../Navbar/Navbar';
import Navigate from '../Router/Navigate';
import {clearActive, setActiveLink} from '../../utils/activeLink';
import {signin, signup} from "../../utils/dictionary";
import {register} from "../../Domain/UserLibrary";

const formRegister = `
            <div id="message">
              
            </div>
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-xl-10">
                    <div class="card rounded-3 text-black" id="backseparator">
                        <div class="row g-0">
                        
                            <div class="">
                                
                                <div class="p-md-5 mx-md-4">
                                    <div class="text-center">
                                        <h1>Bienvenu sur notre site!</h1>                                        
                                    </div>
                                    <div class="d-flex align-items-center justify-content-center pb-4">
                                            <p class="mb-0 me-2">Vous avez deja un compte?</p>
                                            <button type="button" class="btn btn-outline-primary rounded-pill" id="login">${signin}</button>
                                    </div>
                                        <div class="form-outline mb-4">
                                            <label class="form-label" for="form2Example11">Votre photo de profil</label>
                                            <input type="file" accept="image/*" id="pfp" class="form-control"
                                                placeholder="Choisissez votre photo de profil..." />
                                        </div>
                                        
                                        <div class="form-outline mb-4">
                                            <label class="form-label" for="form2Example11">Prénom</label>
                                            <input type="text" id="firstname" class="form-control"
                                                placeholder="Entrez votre prénom..." />
                                        </div>
                                        
                                        <div class="form-outline mb-4">
                                            <label class="form-label" for="form2Example11">Nom</label>
                                            <input type="text" id="lastname" class="form-control"
                                                placeholder="Entrez votre nom..." />
                                        </div>
                                        
                                        <div class="form-outline mb-4">
                                            <label class="form-label" for="form2Example11">E-mail</label>
                                            <input type="email" id="email" class="form-control"
                                                placeholder="Entrez votre adresse e-mail..." />
                                        </div>
                                        
                                        <div class="form-outline mb-4">
                                            <label class="form-label" for="form2Example11">Numéro de GSM</label>
                                            <input type="number" id="GSM" class="form-control"
                                                placeholder="Entrez votre numéro de GSM..." />
                                        </div>

                                        <div class="form-outline mb-4">
                                            <label class="form-label" for="form2Example22">Mot de passe</label>
                                            <input type="password" id="pwd" class="form-control"
                                                placeholder="Entrez votre mot de passe..." />
                                        </div>
                                        
                                        <div class="form-outline mb-4">
                                            <label class="form-label" for="form2Example22">Confirmation du mot de passe</label>
                                            <input type="password" id="pwdConf" class="form-control"
                                                placeholder="Confirmez votre mot de passe..." />
                                        </div>
                                        
                                        <div class="text-center pt-1 mb-5 pb-1">
                                            <button class="btn btn-success rounded-pill"
                                                id="register" type="button" style="color : white">${signup}</button>
                                        </div>
                                </div>
                            </div>                         
                        </div>
                    </div>
                </div>
            </div>           
`;

const RegisterPage = () => {
  const user = getAuthenticatedUser();
  if (user !== undefined) {
    Navigate('/profile');
  }
  clearPage();
  setActiveLink('registerPage');

  Navbar();
  const main = document.querySelector('main');
  main.innerHTML = formRegister;

  goToLoginPage();

  register();

};

function goToLoginPage() {
  const btn = document.getElementById('login');

  btn.addEventListener('click', async (e) => {
    e.preventDefault();
    clearActive();
    Navigate('/login');
  });
}


export default RegisterPage;
