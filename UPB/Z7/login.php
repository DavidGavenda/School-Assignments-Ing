<?php 

session_start(); 


if (isset($_POST['uname']) && isset($_POST['password'])) {

    $uname = $_POST['uname'];
    $pass = $_POST['password'];

    echo "<h2>" . $uname . "</h2>";
    echo "<h2>" . $pass . "</h2>";
}