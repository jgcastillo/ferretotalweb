function handleMessage1(data) {

    var status = data.status;

    if (status == 1) {
        $('.display11').html(function() {
            $('.display11').show();
            return '<p> Boton: ' + data.botonId + '</p> <p>Ubicación: ' + data.ubicacion + '</p><p>Contador: ' + data.counter + '</p>';
        });
    }

    if (status == 2) {
        $('.display12').html(function() {
            $('.display11').hide();
            $('.display12').show();
            return '<p> Boton: ' + data.botonId + '</p> <p>Ubicación: ' + data.ubicacion + '</p><p>Contador: ' + data.counter + '</p>';
        });
    }

    if (status == 3) {
        $('.display13').html(function() {
            $('.display12').hide();
            $('.display13').show();
            return '<p> Boton: ' + data.botonId + '</p> <p>Ubicación: ' + data.ubicacion + '</p><p>Contador: ' + data.counter + '</p>';
        });
    }

    if (status == 4) {
        $('.display11').hide();
        $('.display12').hide();
        $('.display13').hide();
    }

}

function handleMessage2(data) {

    var status = data.status;

    if (status == 1) {
        $('.display21').html(function() {
            $('.display21').show();
            return '<p> Boton: ' + data.botonId + '</p> <p>Ubicación: ' + data.ubicacion + '</p><p>Contador: ' + data.counter + '</p>';
        });
    }

    if (status == 2) {
        $('.display22').html(function() {
            $('.display21').hide();
            $('.display22').show();
            return '<p> Boton: ' + data.botonId + '</p> <p>Ubicación: ' + data.ubicacion + '</p><p>Contador: ' + data.counter + '</p>';
        });
    }

    if (status == 3) {
        $('.display23').html(function() {
            $('.display22').hide();
            $('.display23').show();
            return '<p> Boton: ' + data.botonId + '</p> <p>Ubicación: ' + data.ubicacion + '</p><p>Contador: ' + data.counter + '</p>';
        });
    }

    if (status == 4) {
        $('.display21').hide();
        $('.display22').hide();
        $('.display23').hide();
    }

}