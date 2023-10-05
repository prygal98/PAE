import 'bootstrap/dist/css/bootstrap.min.css';
import {getAuthenticatedUser} from "../../utils/auths";
import {home, homeLogo, propose, signin, signup} from "../../utils/dictionary";

const Footer = async () => {
  const footerWrapper = document.querySelector('#footerWrapper');

  const footer = `
        <!-- Footer -->
            <footer class="page-footer font-small bg-dark text-white pt-4">
            <!-- Footer Links -->
                <div class="container-fluid text-center text-md-left">
                <!-- Grid row -->
                    <div class="row">
                        <!-- Grid column -->
                        <div class="col-md-6 mt-md-0 mt-3">
                            <!-- Content -->
                            <h5 class="text-uppercase">RessourceRie</h5>
                            <p>Rue de Heuseux 77ter, 4671 Blégny</p>
                          
                        </div>
                         <!-- Grid column -->
                        <hr class="clearfix w-100 d-md-none pb-3">
                        <!-- Grid column -->
                        <div class="col-md-3 mb-md-0 mb-3">
                            <!-- Links -->
                            <h5 class="text-uppercase">Liens</h5>

                                <ul class="list-unstyled" id="links">
                                    
                                </ul>
                        </div>
                        <div class="col-md-3 mb-md-0 mb-3">
                            <h5 class="text-uppercase">Conditions générales d'utilisation</h5>
                                <ul class="list-unstyled">
                                    <li>
                                        <a href="#" class="nav-item nav-link lienFooter" data-uri="/cgu">CGU</a>
                                    </li>
                                </ul>
                        </div>
                        <!-- Grid column -->
                    </div>
                    <!-- Grid row -->
                </div>
                <!-- Footer Links -->

                <!-- Copyright -->
                <div class="footer-copyright text-center py-3">©2023 Copyright:
                    <a href="/" class="lienFooter"> ${homeLogo}</a>
                </div>
                <!-- Copyright -->

        </footer>
        <!-- Footer -->
    `
  footerWrapper.innerHTML = footer;
  const user = await getAuthenticatedUser();
  if (user === undefined) {
    const links = document.getElementById('links');
    links.innerHTML += `
               <li>
                 <a href="/" class="nav-item nav-link lienFooter" ><i class="bi bi-house-door"></i> ${home}</a>
             </li>
             <li>
                 <a href="/login" class="nav-item nav-link lienFooter" ><i class="bi bi-box-arrow-in-right"></i> ${signin}</a>
             </li>
             <li>
                 <a href="/register" class="nav-item nav-link lienFooter"><i class="bi bi-person-plus"></i> ${signup}</a>
             </li>       
        `;

  } else {
    const links = document.getElementById('links');
    links.innerHTML += `
              <li>
                <a href="/" class="nav-item nav-link lienFooter" data-uri=""><i class="bi bi-house-door"></i> ${home}</a>
             </li>
             <li>
                <a href="/profile" class="nav-item nav-link lienFooter" data-uri=""><i class="bi bi-box-arrow-in-right"></i> Mon profil</a>
             </li>
             <li>
                <a href="/propose" class="nav-item nav-link lienFooter" data-uri=""><i class="bi bi-person-plus"></i> ${propose}</a>
             </li>         
        `;
  }
}

export default Footer;