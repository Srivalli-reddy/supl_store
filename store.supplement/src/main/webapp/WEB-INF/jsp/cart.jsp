<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Shopping Cart</title>

    <!-- Bootstrap CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>

    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            padding-top: 60px;
        }
        .cart-table {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .product-img {
            width: 80px;
            height: 80px;
            object-fit: contain;
            border-radius: 8px;
            background-color: #fff;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary fixed-top">
    <div class="container">
        <a class="navbar-brand" href="/customer/dashboard">Supplement Store</a>
        <div class="d-flex ms-auto align-items-center">
            <a href="/customer/dashboard" class="btn btn-light me-3">← Continue Shopping</a>
            <a href="/logout" class="btn btn-danger">Logout</a>
        </div>
    </div>
</nav>

<div class="container">
    <h2 class="mb-4">Your Shopping Cart</h2>

    <c:if test="${empty cartItems}">
        <div class="alert alert-info" role="alert">
            Your cart is empty. <a href="/customer/dashboard" class="alert-link">Browse products</a> to add items!
        </div>
    </c:if>

    <c:if test="${not empty cartItems}">
        <table class="table cart-table">
            <thead class="table-light">
            <tr>
                <th>Product</th>
                <th>Name</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Subtotal</th>
                <th>Remove</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${cartItems}">
                <tr>
                    <td>
                        <c:if test="${not empty item.product.imageUrl}">
                            <img src="${item.product.imageUrl}" alt="${item.product.name}" class="product-img" />
                        </c:if>
                    </td>
                    <td>${item.product.name}</td>
                    <td>₹${item.product.price}</td>
                    <td>${item.quantity}</td>
                    <td>₹${item.product.price * item.quantity}</td>
                    <td>
                        <form action="/customer/cart/delete/${item.id}" method="post" style="display:inline;">
                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Remove this item?');">
                                🗑 Remove
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
            <tfoot>
            <tr>
                <th colspan="4" class="text-end">Total:</th>
                <th colspan="2" class="text-success fw-bold">₹${total}</th>
            </tr>
            </tfoot>
        </table>

        <!-- You can add a checkout button here -->
        <div class="d-flex justify-content-end">
            <a href="#" class="btn btn-success btn-lg">Proceed to Checkout</a>
        </div>
    </c:if>
</div>

<!-- Bootstrap Bundle JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
