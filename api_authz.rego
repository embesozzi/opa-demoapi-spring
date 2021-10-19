package http.authz

default allow = false

allow {
	input.method == "GET"
	input.path[_] == "products"
	resource_access_claim[app_name].roles[_] = "read.product"
}

allow {
	input.method == "POST"
	input.path[_] == "products"
	resource_access_claim[app_name].roles[_] = "write.product"
}

resource_access_claim := resource_access {
	auth_header := input.headers.authorization
	startswith(auth_header, "Bearer ")
	bearer_token := substring(auth_header, count("Bearer "), -1)
	[_, claims, _] := io.jwt.decode(bearer_token)
	resource_access := claims.resource_access 
}

## Configuration
app_name:= "product-api"

