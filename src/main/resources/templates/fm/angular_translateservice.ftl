constructor(private translate: TranslateService) {
		this.translate.addLangs(['en', 'fr']);
		this.translate.setDefaultLang('fr');
		this.translate.use('en');
} 
