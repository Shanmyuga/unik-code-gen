<mat-sidenav-container class="sidenav-container">
    <mat-sidenav #${leftNavId} mode="side" opened class="leftnav-container">
      <${leftNav} (${leftNavToggleEvent})="${leftNavId}.toggle()"
                    (${leftNavPushDataEvent})="${rightNavId}.injectData($event)">
      </${leftNav}>
    </mat-sidenav>

    <mat-sidenav-content>
      <p><button mat-button (click)="${leftNavId}.toggle()">Toggle</button></p>
      <${rightNav} #${rightNavId}></${rightNav}>
    </mat-sidenav-content>
</mat-sidenav-container>