jQuery(document).ready(function() {
    let usersTable, itemsTable;

    let loadUserTbl = function() {
        usersTable = $('#usersTable').DataTable({
            select: true,
            "ajax": {
                "url": "/getUserData",
                "dataSrc": ""
            },
            "columns": [
                {data: "card.cardNumber", title: "Card ID"},
                {data: "name", title: "Name"},
                {data: "address", title: "Address"},
                {data: "phoneNum", title: "Phone #"},
                {data: "balance", title: "Balance"}
            ]
        });
    }

    loadUserTbl();

    $('#usersTable tbody').on( 'click', 'tr', function () {
        console.log( usersTable.row( this ).data() );
    } );

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
        console.log( itemsTable.row( this ).data() );
    });

    $('#updateBalanceBtn').click( function () {
        $.get("/updateBalances", function (data) {
           console.log(data);
        });
        refreshAll();
    });

    let refreshAll = function () {
        usersTable.destroy();
        itemsTable.destroy();
        loadUserTbl();
        loadItemsTbl();
    }
});