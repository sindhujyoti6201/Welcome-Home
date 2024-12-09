$(document).ready(function () {
    // Set current year in footer
    $('#year').text(new Date().getFullYear());

    $('#searchButton').click(function () {
        var clientName = $('#clientName').val().trim();
        var orderId = $('#orderId').val().trim();

        if (clientName === '' && orderId === '') {
            $('#results').html('<p class="text-danger">Please enter either a Client Username or Order ID to search.</p>');
            return;
        }

        // Initialize the URL to call based on input
        var url = '';
        if (clientName && orderId) {
            url = '/orders/search?username=' + encodeURIComponent(clientName) + '&orderId=' + encodeURIComponent(orderId);
        } else if (clientName) {
            url = '/orders/search?username=' + encodeURIComponent(clientName);
        } else if (orderId) {
            url = '/orders/search?orderId=' + encodeURIComponent(orderId);
        }

        // Make the AJAX call to the appropriate API endpoint
        $.ajax({
            url: url,
            type: 'GET',  // Method GET for fetching data
            contentType: 'application/json',  // Set the content type to application/json
            dataType: 'json',  // Specify the data type expected in the response
            success: function (data) {
                if (data.length === 0) {
                    if (clientName) {
                        $('#results').html('<p class="text-warning">No orders placed by the username "' + clientName + '"</p>');
                    } else if (orderId) {
                        $('#results').html('<p class="text-warning">No orders available for Order ID "' + orderId + '"</p>');
                    }
                } else {
                    // If orders are found, display the order details
                    var resultHtml = '<h4>Order Results:</h4><ul class="list-group">';
                    data.forEach(function (order) {
                        resultHtml += '<li class="list-group-item">';
                        resultHtml += 'Order ID: ' + order.orderId + '<br>';
                        resultHtml += 'Order Date: ' + order.orderDate + '<br>';
                        resultHtml += 'Order Notes: ' + order.orderNotes + '<br>';
                        resultHtml += 'Supervisor: ' + order.supervisor + '<br>';
                        resultHtml += 'Ordered By: ' + order.orderedBy + '<br>';
                        resultHtml += 'Order Status: ' + order.orderStatus + '<br>';
                        resultHtml += 'Delivered By: ' + order.deliveredBy + '<br>';
                        resultHtml += 'Item ID: ' + order.itemId + '<br>';
                        resultHtml += 'Item Description: ' + order.iDescription + '<br>';
                        resultHtml += 'Has Pieces: ' + (order.hasPieces ? 'Yes' : 'No') + '<br>';

                        if (order.hasPieces && order.pieceNum && order.pieceNum.length > 0) {
                            resultHtml += '<h5>Pieces:</h5><ul>';
                            order.pieceNum.forEach(function (piece) {
                                resultHtml += '<li>Piece Number: ' + piece.pieceNum + ' - ' + piece.pieceDescription + ' stored at roomNum: ' + piece.roomNum + ' and shelfNum: ' + piece.shelfNum + '</li>';
                            });
                            resultHtml += '</ul>';
                        }

                        resultHtml += '</li>';
                    });
                    resultHtml += '</ul>';
                    $('#results').html(resultHtml);
                }
            },
            error: function (xhr, status, error) {
                console.error(error);  // Log the error to the console for debugging
                $('#results').html('<p class="text-danger">Error occurred while searching for orders. Please try again.</p>');
            }
        });
    });
});
