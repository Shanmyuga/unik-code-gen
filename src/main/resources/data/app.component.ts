import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core'; 

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
	title = 'angularexercise';
  
	constructor(private translate: TranslateService) {
		this.translate.addLangs(['en', 'fr']);
		this.translate.setDefaultLang('fr');
		this.translate.use('en');
	} 

	switchLang(ev) {
		this.translate.use(ev.target.value);
	}
}
