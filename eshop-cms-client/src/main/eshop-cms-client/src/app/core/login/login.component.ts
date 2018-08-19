import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { AuthenticationService } from '../auth/authentication.service';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  startYear: number;
  currentYear: number;

  loginData: { username: string; password: string };
  loading = false;
  returnUrl: string;

  error = false;
  errorMsg = null;

  constructor(
    private route: ActivatedRoute,
    private authenticationService: AuthenticationService,
    private router: Router) { }

  ngOnInit() {
    this.startYear = 2018;
    this.currentYear = new Date().getFullYear();
    this.loginData = { username: '', password: '' };
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    if (this.authenticationService.isLogged()) {
      this.router.navigateByUrl('/');
    }
  }

  login() {
    this.authenticationService.login(this.loginData.username, this.loginData.password)
      .subscribe((data: any) => {
        this.loading = false;
        this.router.navigate([this.returnUrl]);
      },
        (error: any) => {
          this.loading = false;
          this.error = true;
          this.errorMsg = error.status === 401 ? 'login.error.401' : 'login.error.general';
        });
  }

}
