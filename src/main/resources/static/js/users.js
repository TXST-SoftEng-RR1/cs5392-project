jQuery(document).ready(function() {
    let userTable, itemsTable, uid, userItem, libraryItem;

    let loadUserTbl = function() {
        uid = $('#userId').val();
        console.log("uid: " + uid);

        userTable = $('#userTable').DataTable({
            select: true,
            "ajax": {
                "url": "/getSingleUserData?userCardNumber=" + uid,
                "dataSrc": ""
            },
            "columns": [
                { data: "itemNumber", title: "Item #" },
                { data: "author", title: "Author" },
                { data: "title", title: "Title" },
                { data: "genre", title: "Genre" },
                { data: "publisher", title: "Publisher" },
                { data: "value", title: "Value ($)" },
                { data: "checkoutDate", title: "Checkout Date" },
                { data: "dueDate", title: "Due Date" },
            ]
        });

        $.get("/getUserBalance?userCardNumber=" + uid, function (data) {
            console.log("User " + uid + " has a balance of " + data);
            $('#userBalance').html("User balance: $" + data);
        });
    }

    $('#loadDataBtn').click(function () {
        if(userTable) {
            userTable.destroy();
        }
        loadUserTbl();
    });

    $('#payBalanceBtn').click(function () {
        uid = $('#userId').val();
        console.log("uid: " + uid);
        $.get("/payBalance?userCardNumber=" + uid, function (data) {
            alert("Balance paid " + data + " for user # " + uid);
        });
        refreshAll();
    });

    let loadItemsTbl = function() {
        itemsTable = $('#itemsTable').DataTable({
            select: true,
            "ajax": {
                "url": "/getItemData",
                "dataSrc": ""
            },
            "columns": [
                {data: "itemNumber", title: "Item #"},
                {data: "author", title: "Author"},
                {data: "title", title: "Title"},
                {data: "genre", title: "Genre"},
                {data: "publisher", title: "Publisher"},
                {data: "value", title: "Value ($)"},
                {data: "checkoutDate", title: "Checkout Date"},
                {data: "dueDate", title: "Due Date"},
            ]
        });
    }
    loadItemsTbl();

    $('#itemsTable tbody').on( 'click', 'tr', function () {
        libraryItem = itemsTable.row( this ).data()
        console.log(libraryItem);
        console.log(libraryItem.itemNumber + " selected by user #" + uid + " for checkout");
    });

    $('#userTable').on( 'click', 'tr', function () {
        userItem = userTable.row( this ).data();
        console.log(userItem);
        console.log(userItem.itemNumber + " selected for user #" + uid);
    });

    $('#returnItemBtn').click( function () {
       $.get("returnItem?itemNumber=" + userItem.itemNumber + "&userCardNumber=" + uid, function (data) {
           alert("Item #" + userItem.itemNumber + " return status: " + data);
       });
        refreshAll();
    });

    $('#checkoutItemBtn').click(function () {
        if ((libraryItem.isRequested && libraryItem.requestingUserId == uid) || !(libraryItem.isRequested)) {
            $.get("checkoutItem?itemNumber=" + libraryItem.itemNumber + "&userCardNumber=" + uid, function (data) {
                alert("User #" + uid + " checked out item #" + libraryItem.itemNumber + ". Status: " + data);
            });
            refreshAll();
        } else {
            alert("Someone else got here first suckaaa!");
        }
    });

    $('#refreshUsrDataBtn').click(function () {
       userTable.destroy();
       loadUserTbl();
    });

    $('#refreshLibDataBtn').click(function () {
       itemsTable.destroy();
       loadItemsTbl();
    });

    let refreshAll = function () {
        userTable.destroy();
        loadUserTbl();
        itemsTable.destroy();
        loadItemsTbl();
    }

    $('#renewItemBtn').click(function () {
        console.log("Renewing item.");
        if (userItem.isRequested) {
            alert("Someone has requested this item; cannot renew. Please return it!");
        } else {
            $.get("/renewItem?itemNumber=" + userItem.itemNumber + "&userCardNumber=" + uid, function (data) {
                alert("Item #" + userItem.itemNumber + " renewed status: " + data);
            });
            refreshAll();
        }
    });

    $('#requestItemBtn').click(function () {
       console.log("Requesting item.");
       $.get("/requestItem?itemNumber=" + libraryItem.itemNumber + "&userCardNumber=" + uid, function (data) {
          alert("Item #" + libraryItem.itemNumber + " request status: " + data);
       });
        refreshAll();
    });

});