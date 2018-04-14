var app = angular.module('chatApp', ['ngMaterial']);
app.controller('chatController', function ($scope) {

    $scope.messages = [
        {
            'sender': 'USER',
            'text': 'hello'
    },
        {
            'sender': 'BOT',
            'text': 'hi wassup'
    }
    ]
});