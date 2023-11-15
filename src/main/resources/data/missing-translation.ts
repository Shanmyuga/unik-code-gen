import {MissingTranslationHandler, MissingTranslationHandlerParams} from '@ngx-translate/core';

export class NbMissingTranslationHandler implements MissingTranslationHandler {
    handle(params: MissingTranslationHandlerParams) {
       return 'not found-' + params;
    }
}
