library teamag.teamag_module;

import 'package:angular/angular.dart';
import 'package:teamag/controller/navbar_controller.dart';
import 'package:teamag/component/navbar_component.dart';
import 'package:teamag/routing/teamag_router.dart';

class TeamagModule extends Module {
  TeamagModule() {
    type(NavBarController);
    type(NavBarComponent);
    value(RouteInitializerFn, teamagRouteInitializer);
  }
}