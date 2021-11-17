<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<link rel="stylesheet" href='<c:url value="/css/bootstrap.min.css"/>' />

<title>Đăng ký</title>
</head>
<body>

	<div class="container">
		<div class="row justify-content-center">
			<h2 class="text-center text-uppercase mt-5 fw-bold">Đăng ký</h2>
			<div class="col-xl-4 col-md-6 align-items-center">
				<form:form class="m-4" method="post" action="save-data" modelAttribute="taikhoan" >
					<div class="form-group">
						<label for="name">Số điện thoại</label> <input type="text"
							class="form-control" name="" />
					</div>
					<div class="form-group">
						<label for="pwd">Điểm</label> <input type="text"
							class="form-control" name="mark" />
					</div>
					<div class="d-grid gap-2 mt-5">
						<button type="submit" class="btn btn-primary btn-block">Submit</button>
					</div>
				</form:form>

			</div>
		</div>
	</div>

	<script src='<c:url value = "/js/jquery-3.6.0.min.js"/>'></script>
	<script src='<c:url value = "/js/bootstrap.bundle.min.js"/>'></script>
</body>
</html>