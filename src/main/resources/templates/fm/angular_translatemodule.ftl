TranslateModule.forRoot({
      missingTranslationHandler: {provide: MissingTranslationHandler, useClass: NbMissingTranslationHandler},
      loader: {
        provide: TranslateLoader,
        useFactory: (httpClient: HttpClient) => new TranslateHttpLoader(httpClient, 'assets/i18n/lang/', '.json'),
        deps: [HttpClient]
      }
})
