app.controller('playersController', ['app_config', '$scope', '$http', '$window', '$sce', function(app_config, $scope, $http, $window, $sce) {

    $scope.getPlayer = function(id) {
        $window.location.href = '#/player?id=' + id;
    };

    $scope.getPlayers = function(tableState) {
        $scope.isLoading = true;
        var pagination = tableState.pagination;
        var start = pagination.start || 0;
        var number = pagination.number || $scope.itemsByPage;

        var url = app_config.baseUrl + '/players?page='+( 1+(start/number))+'&size='+number;
        $http.get(url).success(function (pageable) {
            $scope.pageable = pageable;
            $scope.items = pageable.content;
            tableState.pagination.numberOfPages = pageable.totalPages;
            $scope.isLoading = false;

            for (var i = 0; i < $scope.items.length; i++) {
                var player = $scope.items[i]
                player.flagSrc = getFlagSrc(player.nationality);
            }
        });
    };

    $scope.getAbility = function(ability) {
        return $sce.trustAsHtml(getAbilitySVG(ability));
    };

}]);

app.controller('playerController', ['app_config', '$scope', '$http', '$routeParams', '$sce', function(app_config, $scope, $http, $routeParams, $sce) {

    $scope.progressPlayer = function() {
        var url = app_config.baseUrl + '/progress?id=' + $routeParams.id;
        $http.get(url).success(function (response) {
            initPlayer($scope, response);
        });
    };

    var url = app_config.baseUrl + '/player?id=' + $routeParams.id;
    $http.get(url).success(function (response) {
        initPlayer($scope, response);
    });

    $scope.getAbility = function(ability) {
        return $sce.trustAsHtml(getAbilitySVG(ability));
    };

}]);

function getAbilitySVG(ability) {

    var svg = '<svg height="30"><g class="scaleSVG">';
    var value = ability / 100;
    var sepertor = 10 + 80;

    var xPoints = [ 38, 50, 77, 57, 62, 38, 15, 20, 0, 27 ];
    var yPoints = [ 0, 26, 29, 47, 76, 61, 76, 47, 29, 26 ];

    for (var i = 0; i < 10; i++) {
        var color = null;

        if(i < value){
            color = '240, 144, 16';
        }
        else{
            color = '212,212,212';
        }

        var polygon = '<polygon points="';
        for (var j = 0; j < xPoints.length; j++) {
            var x = xPoints[j] + (i * sepertor);
            var y = yPoints[j];
            polygon = polygon + x + ',' + y + ' ';
        }
        polygon = polygon + '" style="fill:rgb('+color+');stroke:black;stroke-width:3;fill-rule:nonzero;"/>';
        svg = svg + polygon;
    }

    svg = svg + '</g></svg>';
    return svg;
};

function getFlagSrc(nationality) {
    return '/images/flags_iso/16/' + nationality.isoCode.toLowerCase()+ '.png';
};

function initPlayer($scope, response) {
  $scope.player = response;
      $scope.flagSrc = getFlagSrc(response.nationality);

      console.log(response);

      $scope.technicalAttributes = {
          corners : response.attributes.corners,
          crossing : response.attributes.crossing,
          dribbling : response.attributes.dribbling,
          finishing : response.attributes.finishing,
          firstTouch : response.attributes.firstTouch,
          freekicks : response.attributes.freekicks,
          heading : response.attributes.heading,
          longShots : response.attributes.longShots,
          longthrows : response.attributes.longthrows,
          marking : response.attributes.marking,
          passing : response.attributes.passing,
          penaltyTaking : response.attributes.penaltyTaking,
          tackling : response.attributes.tackling,
          technique : response.attributes.technique
      };

      $scope.mentalAttributes = {
          aggression : response.attributes.aggression,
          anticipation : response.attributes.anticipation,
          bravery : response.attributes.bravery,
          composure : response.attributes.composure,
          concentration : response.attributes.concentration,
          creativity : response.attributes.creativity,
          decisions : response.attributes.decisions,
          determination : response.attributes.determination,
          flair : response.attributes.flair,
          influence : response.attributes.influence,
          offTheBall : response.attributes.offTheBall,
          positioning : response.attributes.positioning,
          teamwork : response.attributes.teamwork,
          workrate : response.attributes.workrate
      };

      $scope.physicalAttributes = {
          acceleration : response.attributes.acceleration,
          agility : response.attributes.agility,
          balance : response.attributes.balance,
          jumping : response.attributes.jumping,
          naturalFitness : response.attributes.naturalFitness,
          pace : response.attributes.pace,
          stamina : response.attributes.stamina,
          strength : response.attributes.strength
      };

      $scope.goalKeeperAttributes = {
          aerialAbility : response.attributes.aerialAbility,
          commandOfArea : response.attributes.commandOfArea,
          communication : response.attributes.communication,
          eccentricity : response.attributes.eccentricity,
          firstTouch : response.attributes.firstTouch,
          freekicks : response.attributes.freekicks,
          handling : response.attributes.handling,
          kicking : response.attributes.kicking,
          oneOnOnes : response.attributes.oneOnOnes,
          penaltyTaking : response.attributes.penaltyTaking,
          reflexes : response.attributes.reflexes,
          rushingout : response.attributes.rushingout,
          tendencyToPunch : response.attributes.tendencyToPunch,
          throwing : response.attributes.throwing
      };

      $scope.leftFoot = getDescriptionForPositionValue(response.position.leftFoot);
      $scope.rightFoot = getDescriptionForPositionValue(response.position.rightFoot);

      function getDescriptionForPositionValue(value){
          var description = "";
          if(value == 20)
              description = "Natural";
          else if(value > 15)
              description = "Very Good";
          else if(value > 10)
              description = "Ok";
          else if(value > 8)
              description = "Not Great";
          else
              description = "Unconvincing";
          return description;
      };

      $scope.positions = getPositions(response.position);
      function getPositions(position) {

          var positions = [];

          if(position.st >1){
               positions.push({
                  position: "Striker",
                  description : getDescriptionForPositionValue(position.st)
              });
          };
          if(position.amc >1){
              positions.push({
                  position: "Attacking Midfielder (Center)",
                  description : getDescriptionForPositionValue(position.amc)
              });
          };
          if(position.aml >1){
              positions.push({
                  position: "Attacking Midfielder (Left)",
                  description : getDescriptionForPositionValue(position.aml)
              });
          }
          if(position.amr >1){
              positions.push({
                  position: "Attacking Midfielder (Right)",
                  description : getDescriptionForPositionValue(position.amr)
              });
          }
          if(position.mr >1){
              positions.push({
                  position: "Midfielder (Right)",
                  description : getDescriptionForPositionValue(position.mr)
              });
          }
          if(position.ml >1){
              positions.push({
                  position: "Midfielder (Left)",
                  description : getDescriptionForPositionValue(position.ml)
              });
          }
          if(position.mc >1){
              positions.push({
                  position: "Midfielder (Center)",
                  description : getDescriptionForPositionValue(position.mc)
              });
          }
          if(position.wbr >1){
              positions.push({
                  position: "Wing Back (Right)",
                  description : getDescriptionForPositionValue(position.wbr)
              });
          }
          if(position.wbl >1){
             positions.push({
                 position: "Wing Back (Left)",
                 description : getDescriptionForPositionValue(position.wbl)
             });
          }
          if(position.dm >1){
              positions.push({
                  position: "Defensive Midfielder (Center)",
                  description : getDescriptionForPositionValue(position.dm)
              });
          }
          if(position.dr >1){
              positions.push({
                  position: "Defender (Right)",
                  description : getDescriptionForPositionValue(position.dr)
              });
          }
          if(position.dl >1){
              positions.push({
                  position: "Defender (Left)",
                  description : getDescriptionForPositionValue(position.dl)
              });
          }
          if(position.dc >1){
              positions.push({
                  position: "Defender (Center)",
                  description : getDescriptionForPositionValue(position.dc)
              });
          }
          if(position.sw >1){
              positions.push({
                  position: "Sweeper",
                  description : getDescriptionForPositionValue(position.sw)
              });
          }
          if(position.gk >1){
              positions.push({
                  position: "Goal Keeper",
                  description : getDescriptionForPositionValue(position.gk)
              });
          }

          return positions;
      }

      function getDescriptionForPositionValue(value){
          var description = "";
          if(value == 20)
              description = "Natural";
          else if(value > 15)
              description = "Very Good";
          else if(value > 10)
              description = "Ok";
          else if(value > 8)
              description = "Not Great";
          else
              description = "Unconvincing";
          return description;
      }
};