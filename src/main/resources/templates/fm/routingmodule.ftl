${imports}

const ${routeName}: Routes = [
  ${routes}
];

@NgModule({
  exports: [ ${modExports} ],
  imports: [
    ${modImports}
  ],
  declarations: [ ${declarations} ]
})
export class ${moduleName} { }