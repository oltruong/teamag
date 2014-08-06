module.exports = function(config){
    config.set({
    basePath : '../',

    files : [
      'angularjs/lib/angular/angular.js',
      'angularjs/lib/angular/angular-*.js',
      '../../../test/webapp/lib/angular/angular-mocks.js',
      'angularjs/js/**/*.js',
      '../../../test/webapp/unit/**/*.js'
    ],

    exclude : [
      'app/lib/angular/angular-loader.js',
      'app/lib/angular/*.min.js',
      'app/lib/angular/angular-scenario.js'
    ],

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['Chrome'],

    plugins : [
            'karma-junit-reporter',
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine'
            ],

    junitReporter : {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    }

})}
