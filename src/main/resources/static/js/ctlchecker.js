jQuery(document).ready(function() {
    let ctlModel = $("#ctlModelFile");
    let modelName;

    ctlModel.change(function getFile(event) {
        modelName = ctlModel.val().replace('C:\\fakepath\\', '');
        console.log("Kripke model file name: " + modelName);

        if (!modelName.includes(".json")) {
            $("#invalidModelModal").modal('show');
            return {
                error: true,
                message: "Kripke model must be valid JSON file."
            }
        }

        const input = event.target;
        if ('files' in input && input.files.length > 0) {
            placeFileContent($('#content-target'), input.files[0]);
        }
    });

    function placeFileContent(target, file) {
        readFileContent(file).then(content => {
            // try to parse as JSON to force an error if it is not valid
            let json = $.parseJSON(content);
            console.log("updating textbox...");
            target.val(content);
        }).catch(error => {
            $('#invalidModelModal').modal('toggle');
            console.log(error);
        })
    }

    function readFileContent(file) {
        const reader = new FileReader()
        return new Promise((resolve, reject) => {
            reader.onload = event => resolve(event.target.result)
            reader.onerror = error => reject(error)
            reader.readAsText(file)
        });
    }

    $("#closeInvalidModelModal").click(function () {
        $("#invalidModelModal").modal('toggle');
    });

    $("#closeUsageInstructionsModal").click(function () {
        $("#usageInstructionsModal").modal('toggle');
    });

    $("#submitModel").click(function () {
            $.ajax({
                url: '/uploadModel',
                method: 'POST',
                type: 'POST', // for jQuery < 1.9
                processData: false,
                contentType: 'application/json',
                data: $('#content-target').val(),
                success: handleData,
                error: handleError
            });
        }
    );



    function handleData(data , textStatus, jqXHR  ) {
        console.log(data + textStatus);
        //do some stuff
    }

    function handleError(data, textStatus, jqXHR) {
        console.log(data + textStatus);
    }

});