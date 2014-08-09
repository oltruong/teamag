library teamag.routing;

import 'package:angular/angular.dart';

void teamagRouteInitializer(Router router, RouteViewFactory views) {
  views.configure({
      'add': ngRoute(path: '/add', view: 'view/welcome2.html'), 'home': ngRoute(path: '/add', view: 'view/welcome.html', defaultRoute:'true'),

  });
}