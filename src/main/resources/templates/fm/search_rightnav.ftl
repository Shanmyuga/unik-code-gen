<div id="rightnav-container">
    <h3>${title}</h3>
    <ngx-datatable
      class="material"
      [columnMode]="'force'"
      [headerHeight]="50"
      [footerHeight]="0"
      [rowHeight]="50"
      [scrollbarV]="false"
      [scrollbarH]="true"
      [rows]="${listObject}"
      [columns]="columns">
    </ngx-datatable>
</div>