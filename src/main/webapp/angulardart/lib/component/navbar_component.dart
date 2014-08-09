library teamag.component.navbar_component;

import 'package:angular/angular.dart';

@NgComponent(selector: 'navbar', templateUrl: 'packages/teamag/component/navbar_component.html', cssUrl: 'css/bootstrap.min.css', publishAs: 'nav')
class NavBarComponent {
  @NgAttr('name')
  String name = '';
}