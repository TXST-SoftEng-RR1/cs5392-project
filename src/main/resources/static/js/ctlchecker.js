jQuery(document).ready(function() {
    let ctlModel = $("#ctlModelFile");

    ctlModel.change(function getFile(event) {
        let modelName = ctlModel.val().replace('C:\\fakepath\\', '');
        console.log("Kripke model file name: " + modelName);

        if (!modelName.includes(".json")) {
            $("#invalidModelModal").modal('show');
            return {
                error: true,
                message: "Kripke model must be valid JSON file."
            }
        }

        const input = event.target
        if ('files' in input && input.files.length > 0) {
            placeFileContent(
                document.getElementById('content-target'),
                input.files[0])
        }
    });

    function placeFileContent(target, file) {
        readFileContent(file).then(content => {
            target.value = content
        }).catch(error => console.log(error))
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

});