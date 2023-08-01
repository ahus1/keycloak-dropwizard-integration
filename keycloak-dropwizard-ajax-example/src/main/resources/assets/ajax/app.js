var keycloak = new Keycloak();

keycloak.init({onLoad: 'login-required'})
        .then(function (authenticated) {
            if (!authenticated) {
                alert('not authenticated');
            } else {
                document.getElementById('name').innerHTML = keycloak.idTokenParsed.name;
                document.getElementById('form').style.display = 'unset';
            }
        })
        .catch(function (err) {
            console.log("failed", err);
            alert('failed to initialize');
        });

var loadData = function () {

    var data = {};

    data['date'] = document.getElementsByName('date')[0].value;

    var url = '/draw';

    var req = new XMLHttpRequest();
    req.open('POST', url, true);
    req.setRequestHeader('Accept', 'application/json');
    req.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);

    req.onreadystatechange = function () {
        if (req.readyState === 4) {
            if (req.status === 200) {
                var draw = JSON.parse(req.responseText);
                var html = '';
                for (var i = 0; i < draw.numbers.length; i++) {
                    html += draw.numbers[i] + '<br>';
                }
                document.getElementById('draw').innerHTML = html;
                document.getElementById('drawdate').innerHTML = draw.date;
                document.getElementById('message').innerHTML = '';
                document.getElementById('result').style.display = 'block';
            } else {
                document.getElementById('message').innerHTML = req.responseText
            }
        }
    }

    req.send(JSON.stringify(data));
};

var loadFailure = function (error) {
    document.getElementById('message').innerHTML = '<b>Failed to load data.  Check console log</b>' + error;

};

var reloadData = function () {
    keycloak.updateToken()
            .then(loadData).catch((error) => loadFailure(error));
}
