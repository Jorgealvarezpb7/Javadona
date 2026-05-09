shell := if os() == "windows" { "powershell" } else { "bash" }

default:
    @echo "Supermarket Platform"
    just --list

# Start all services (detached)
up:
    docker compose up -d

# Start all services and rebuild images first
up-build:
    docker compose up -d --build

# Stop all services
down:
    docker compose down

# Stop all services and remove volumes
down-clean:
    docker compose down -v

# Follow logs (all services)
logs:
    docker compose logs -f

# Follow logs for one service: just logs-svc customer-service
logs-svc svc:
    docker compose logs -f {{svc}}

# Build all Docker images
build:
    docker compose build

# Run customer-service in isolation (own MySQL instance)
up-customer:
    docker compose -f customer-service/compose.yml up -d

# Run sales-point-service in isolation
up-sales-point:
    docker compose -f sales-point-service/compose.yml up -d

# Run inventory-service in isolation
up-inventory:
    docker compose -f inventory-service/compose.yml up -d

# Run sales-service in isolation (DB only — other services not available)
up-sales:
    docker compose -f sales-service/compose.yml up -d

down-customer:
    docker compose -f customer-service/compose.yml down

down-sales-point:
    docker compose -f sales-point-service/compose.yml down

down-inventory:
    docker compose -f inventory-service/compose.yml down

down-sales:
    docker compose -f sales-service/compose.yml down

# Build all services (skip tests)
mvn-build:
    cd customer-service    && mvn clean package -DskipTests -q
    cd sales-point-service && mvn clean package -DskipTests -q
    cd inventory-service   && mvn clean package -DskipTests -q
    cd sales-service       && mvn clean package -DskipTests -q
    cd api-gateway         && mvn clean package -DskipTests -q

test-customer:
    cd customer-service && mvn test

test-sales-point:
    cd sales-point-service && mvn test

test-inventory:
    cd inventory-service && mvn test

test-sales:
    cd sales-service && mvn test

# Bootstrap Minikube cluster and build images inside it
k8s-init:
    minikube start --cpus=4 --memory=6144 --disk-size=20g
    minikube addons enable ingress
    eval $(minikube docker-env) \
        && docker build -t customer-service:latest    ./customer-service \
        && docker build -t sales-point-service:latest ./sales-point-service \
        && docker build -t sales-service:latest       ./sales-service \
        && docker build -t inventory-service:latest   ./inventory-service \
        && docker build -t api-gateway:latest         ./api-gateway

# Apply all manifests (MySQL must be ready before services)
k8s-apply:
    kubectl apply -f infrastructure/k8s/namespace.yaml
    kubectl apply -f infrastructure/k8s/secrets/
    kubectl apply -f infrastructure/k8s/configmaps/
    kubectl apply -f infrastructure/k8s/deployments/mysql.yaml
    kubectl wait --for=condition=ready pod -l app=mysql -n supermarket --timeout=90s
    kubectl apply -f infrastructure/k8s/deployments/
    kubectl apply -f infrastructure/k8s/ingress.yaml

# Tear down all K8s resources
k8s-delete:
    kubectl delete namespace supermarket

# Watch pod status
k8s-status:
    kubectl get pods -n supermarket -w

# Stream logs for a deployment: just k8s-logs sales-service
k8s-logs svc:
    kubectl logs -f deployment/{{svc}} -n supermarket

# Register supermarket.local in /etc/hosts (requires sudo)
k8s-hosts:
    echo "$(minikube ip) supermarket.local" | sudo tee -a /etc/hosts

# Starts the React frontend (in development mode)
web-dev:
    cd web && bun run dev
