<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Customer Dashboard</title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>

    <style>
        body {
            margin: 0; min-height: 100vh;
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(to right, #fdfbfb, #ebedee);
            display: flex;
        }

        .sidebar {
            min-width: 250px;
            background: linear-gradient(135deg, #2c3e50, #4ca1af);
            color: #fff;
            display: flex;
            flex-direction: column;
            padding: 30px 20px;
            box-shadow: 2px 0 16px rgba(44,62,80,0.08);
        }

        .sidebar h2 {
            font-size: 1.4rem;
            font-weight: 700;
            border-bottom: 2px solid #fff5;
            padding-bottom: 18px;
            margin-bottom: 1.7rem;
            letter-spacing: 1px;
        }

        .sidebar .sidebar-link {
            color: #eaeff3;
            text-decoration: none;
            font-size: 17px;
            margin-bottom: 24px;
            padding: 6px 10px;
            border-radius: 6px;
            display: block;
            transition: background 0.18s;
            font-weight: 500;
        }
        .sidebar .sidebar-link.active,
        .sidebar .sidebar-link:hover {
            background: #4ca1af99;
            color: #ffd700;
        }
        .sidebar .logout-btn {
            margin-top: auto;
            background: #e74c3c;
            color: #fff;
            border: none;
            border-radius: 7px;
            padding: 9px 16px;
            font-weight: bold;
        }
        .sidebar .logout-btn:hover {
            background: #c0392b;
        }

        .main-content {
            flex: 1;
            padding: 36px 3vw;
        }
        .dashboard-header {
            display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px;
        }
        .dashboard-header .welcome {
            font-size: 1.2rem; font-weight: 600;
        }
        .btn-cart {
            background: #ffcd39; color: #222; font-weight: 600;
        }
        .btn-cart:hover { background: #ffd700; }

        /* Card grid */
        .product-grid .product-card {
            border-radius: 18px;
            box-shadow: 0 6px 24px rgba(44,62,80,0.08);
            transition: transform 0.18s, box-shadow 0.18s;
            background: #fff;
            margin-bottom: 32px;
            height: 100%;
            display: flex;
            flex-direction: column;
        }
        .product-grid .product-card:hover {
            transform: translateY(-4px) scale(1.025);
            box-shadow: 0 12px 38px rgba(76,161,175,0.17);
        }
        .product-img {
            width: 100%; height: 156px; object-fit: contain;
            background: #f7fcfc;
            border-radius: 18px 18px 0 0;
            padding: 22px 0;
        }
        .product-name {
            font-weight: 700; font-size: 1.08rem; color: #222;
        }
        .product-category {
            font-size: 0.93rem; color: #5db7b7; font-weight: 500;
        }
        .product-price {
            color: #10aa44; font-weight: 800; margin-bottom: 10px;
        }
        .add-to-cart-btn {
            margin-top: auto;
            background: #2176ff;
            color: #fff; border-radius: 10px; border: none;
            font-weight: 600; padding: 10px 0; transition: background 0.2s;
        }
        .add-to-cart-btn:hover { background: #114477; }

        @media (max-width: 991.98px) {
            .sidebar { min-width: 80px; padding: 15px 7px; }
            .sidebar h2, .sidebar .user-name { font-size: 1rem; }
            .main-content { padding: 15px 2vw; }
            .dashboard-header { flex-direction: column; align-items: flex-start; gap: 7px;}
        }
    </style>
</head>
<body>
<div class="sidebar">
    <h2>Customer Panel</h2>
    <div class="user-name mb-3">👤 ${username}</div>
    <a class="sidebar-link active" href="#">🛒 Product List</a>
    <a class="sidebar-link" href="/customer/cart">🧺 My Cart</a>
    <a class="sidebar-link" href="#" style="cursor:default;opacity:0.6;">(More coming soon)</a>
    <form action="/logout" method="get">
        <button type="submit" class="logout-btn mt-4 w-100">Logout</button>
    </form>
</div>

<div class="main-content">
    <div class="dashboard-header">
        <div class="welcome">Welcome, <b>${username}</b>!</div>
        <a href="/customer/cart" class="btn btn-cart">🧺 View Cart</a>
    </div>

    <h3 class="mb-4">Available Products</h3>
    <div class="row row-cols-1 row-cols-md-3 row-cols-lg-4 g-4 product-grid">
        <c:forEach var="product" items="${products}">
            <div class="col mb-3">
                <div class="product-card h-100">
                    <c:if test="${not empty product.imageUrl}">
                        <img src="${product.imageUrl}" class="product-img" alt="${product.name}" />
                    </c:if>
                    <div class="p-3 d-flex flex-column h-100">
                        <div class="product-name mb-2">${product.name}</div>
                        <div class="product-category mb-1">${product.category}</div>
                        <div class="product-price mb-1">₹${product.price}</div>
                        <form method="post" action="/customer/cart/add/${product.id}" class="mt-auto w-100">
                            <button type="submit" class="add-to-cart-btn w-100">Add to Cart</button>
                        </form>
                    </div>
                </div>
            </div>
        </c:forEach>
        <c:if test="${empty products}">
            <div class="col-12">
                <div class="alert alert-warning text-center">
                    No products available.
                </div>
            </div>
        </c:if>
    </div>
</div>

</body>
</html>
