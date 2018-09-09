<?php

$con=$mysql_connect(host,username,password,database);
$username=$_GET['IDNumber'];
$password=$_GET['Password'];
$contactNumber=$_GET['ContactNum'];
$fname=$_GET['FirstName'];
$lname=$_GET['LastName'];
$Dob=$_GET['Date_Of_Birth'];
$gender=$_GET['Gender'];
$status=$_GET['Status'];
$email=$_GET['Email'];
$memberNum=$_GET['MemberNumber'];
if($username==''||$password==''||$email=='')
{
	$error_details=''Please feel in all values;
}
	else	
	{
		$query="SELECT * FROM Register WHERE username='$username'OR email='$email'";
		$check=$mysql_fetch_array(mysql_query($con,$query));
		if(isset($check))
		{
			$error_exist='username or email already exist';
			
		}else
		{
			$query="INSERT INTO Register(IDNumber,Password,ContactNum,FirstName,LastName,Date_Of_Birth,Gender,Status,Email,MemberNumber) VALUES('$username,$password,$contactNumber,$fname,$lname,$Dob,$gender,$status,$email,$memberNum')";
			
		}
		if (mysql_query)($con,$query))
		{
			$response=' You have succesfully registered';		
		} else
		{
			$response='oops ! Something went wrong  
			Please try again!';
	}}
		
	$mysql_close($con);
	
	}
?>