var formulaInput = $("#formulaInput");
var stateSelector = $("#stateSelector");

const canvas = document.getElementById("ctlModelCanvas");
const context = canvas.getContext('2d');
const centerX = canvas.width / 2;
const centerY = canvas.height / 2;
const radius = 15;

var stateMap = new Map();
var ctlModel = $("#ctlModelFile");
var modelName;
var CTLRegex = /^([a-z]{1})$|\(*.&*.\)|\(*.\|*.\)|\(*.->*.\)|\[*.U*.]|^false$|^true$|[A-Z]{2}[a-z]{1}/i

jQuery(document).ready(function () {

    context.fillStyle = "white";
    context.fillRect(0, 0, canvas.width, canvas.height);

    console.log("Test Regex validation: ");
    console.log("a: " + CTLRegex.test("a"));
    console.log("true: " + CTLRegex.test("true"));

    /**
     * Pre-validation of CTL formula using regex defining limited rules
     */
    formulaInput.on('input', function () {
        let errorMsg = $(".errorMsg");

        // mismatch
        let formulaVal = String($(this).val());
        if (!formulaVal.match(CTLRegex)) {
            console.log($(this).val());
            errorMsg.removeClass('hidden');
            errorMsg.show();
        } else {
            console.log("Successful formula match!");
            errorMsg.addClass('hidden');
            errorMsg.hide();
        }
    });

    /**
     * Trigger the model upload and parse process
     */
    ctlModel.change(function getFile(event) {
        // clean up prev model if it exists


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


    /**
     * Attempt to parse the model to determine if it is valid JSON
     * If it is, depict the model graphically and textually.
     * @param target
     * @param file
     */
    function placeFileContent(target, file) {
        readFileContent(file).then(content => {
            // try to parse as JSON to force an error if it is not valid
            let json = $.parseJSON(content);

            // check for atoms
            if (!json["kripke-model"].hasOwnProperty('atoms')) {
                alert("Kripke model must contain atoms property.")
                return {
                    error: true,
                    message: "Kripke model must contain atoms property."
                }
            }

            // check for proper transitions
            let transitions = json["kripke-model"].transitions;
            for (let i in transitions) {
                if (transitions[i] !== null && !transitions[i].includes(",")) {
                    alert("Kripke model must contain proper transitions: \"FROM_STATE,TO_STATE\"")
                    return {
                        error: true,
                        message: "Kripke model must contain proper transitions: \"FROM_STATE,TO_STATE\""
                    }
                }
            }

            console.log("updating textbox...");
            target.val(content);
            console.log("drawing model...");
            drawModel(json);
            enableFields();
            populateStateSelector(json);
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

    /**
     * @author Sneha Sirnam
     * Submit the CTL formula to the back-end along with a state
     * to see if the model holds for that state.
     */
    $("#submitFormula").click(function () {
        let formulaData = "{\"formula\": " + "\"" + formulaInput.val() + "\"" +
            ", \"state\": " + "\"" + stateSelector.val() + "\"" + "}";
        let combinedDataObj = "[" + $('#content-target').val() + "," + formulaData + "]";

        $.ajax({
            url: '/validateModel',
            method: 'POST',
            type: 'POST', // for jQuery < 1.9
            processData: false,
            contentType: 'application/json',
            data: combinedDataObj,
            success: handleData,
            error: handleError
        });
    });

    /**
     *
     * @param data
     * @param textStatus
     * @param jqXHR
     */
    function handleData(data, textStatus, jqXHR) {
        console.log(data + " " + textStatus);
        $("#validationResultsArea").val(data);
    }

    function handleError(data, textStatus, jqXHR) {
        console.log(data + " " + textStatus);
    }
});


function drawModel(json) {
    context.fillStyle = "white";
    context.fillRect(0, 0, canvas.width, canvas.height);

    let states = json["kripke-model"].states;
    let atoms = json["kripke-model"].atoms;
    let xOffset, yOffset;
    for (let x = 0; x < states.length; x++) {
        let state = states[x];
        let stateAtoms = "";
        for (let i in atoms) {
            for (let stateKey in atoms[i]) {
                let key = stateKey;
                let data = atoms[i][key];
                console.log("key " + key + " data " + data);
                if (key === state)
                    stateAtoms = data.toString();
            }


        }
        console.log("State: " + state + "; atoms: " + stateAtoms);
        xOffset = (x + 1) * 65;
        yOffset = (x + 1) * 65;
        stateMap.set(state, [xOffset, yOffset]);
        drawState(xOffset, yOffset, state, stateAtoms);
    }

    drawTransitions(json, xOffset, yOffset);
}

function drawState(xOffset, yOffset, label, atoms) {
    context.beginPath();
    context.arc(xOffset, yOffset, radius, 0, 2 * Math.PI, false);
    context.fillStyle = 'green';
    context.fill();
    context.lineWidth = 1;
    context.strokeStyle = '#003300';
    context.stroke();

    context.font = '8pt Calibri';
    context.fillStyle = 'white';
    context.textAlign = 'center';
    context.fillText(atoms, xOffset, yOffset + 3);
    context.fillStyle = 'black';
    context.fillText(label, xOffset, yOffset + 25);
}

function drawTransitions(json, xOffset, yOffset) {
    let transitions = json["kripke-model"].transitions;
    let toggle = -1;
    for (let i in transitions) {
        console.log(transitions[i]);
        const states = transitions[i].split(',');
        let fromCoordinates = stateMap.get(states[0]);
        console.log(fromCoordinates.toString());
        let toCoordinates = stateMap.get(states[1]);
        console.log(toCoordinates.toString());

        drawArrow(fromCoordinates[0], fromCoordinates[1], toCoordinates[0], toCoordinates[1], toggle, xOffset, yOffset);
        toggle *= -1;
    }
}

function drawArrow(fromx, fromy, tox, toy, toggle, xOffset, yOffset) {
    let headlen = 10; // length of head in pixels
    let dx = tox - fromx;
    let dy = toy - fromy;
    let angle = Math.atan2(dy, dx);
    context.beginPath();
    context.moveTo(fromx, fromy + (15 * toggle));
    let sx = ((fromx + tox) / 2) + (-70 * toggle);
    let sy = ((fromy + (15 * toggle) + toy) / 2) + (70 * toggle);
    let ex, ey;
    if (toggle === -1) {
        ex = tox + (toggle * -2);
        ey = toy - radius + (toggle * 2);
    } else if (toggle === 1) {
        ex = tox + (toggle * -2);
        ey = toy + radius + (toggle * 2);
    }


    context.quadraticCurveTo(sx, sy, ex, ey);
    // context.lineTo(tox - headlen * Math.cos(angle - Math.PI / 6), toy - headlen * Math.sin(angle - Math.PI / 6));
    // context.moveTo(tox, toy);
    // context.lineTo(tox - headlen * Math.cos(angle + Math.PI / 6), toy - headlen * Math.sin(angle + Math.PI / 6));
    context.stroke();

    let ang = findAngle(sx, sy, ex, ey);
    context.fillRect(ex, ey, 1, 1);
    drawArrowhead(ex, ey, ang, 5, 5);


}

function drawArrowhead(locx, locy, angle, sizex, sizey) {
    let hx = sizex / 2;
    let hy = sizey / 2;

    context.translate((locx), (locy));
    context.rotate(angle);
    context.translate(-hx, -hy);

    context.beginPath();
    context.moveTo(0, 0);
    context.lineTo(0, 1 * sizey);
    context.lineTo(sizex, hy);
    context.closePath();
    context.fill();

    context.translate(hx, hy);
    context.rotate(-angle);
    context.translate(-locx, -locy);
}

// returns radians
function findAngle(sx, sy, ex, ey) {
    // make sx and sy at the zero point
    return Math.atan2((ey - sy), (ex - sx));
}

function enableFields() {
    formulaInput.prop("disabled", false);
    stateSelector.prop("disabled", false);
}

/**
 * @author Sivaranjani
 * Dynamically populate the state selector dropdown
 * with state values from the Kripke JSON model
 */
function populateStateSelector(json) {
    let states = json["kripke-model"].states;
    removeOptions();
    for (let state in states) {
        stateSelector.append('<option value=' + states[state] + '>' + states[state] + '</option>');
    }
}

function removeOptions() {
    if( stateSelector.has('option').length > 0)
        stateSelector.empty();
    else
        console.log("Select has no options to remove.");

}