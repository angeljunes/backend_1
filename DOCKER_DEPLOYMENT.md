# 🐳 Guía de Despliegue con Docker

## 📋 Requisitos Previos

- Docker instalado (versión 20.10 o superior)
- Docker Compose instalado (versión 2.0 o superior)

## 🚀 Construcción y Ejecución Local

### Opción 1: Usando Docker Compose (Recomendado)

```bash
# Construir y ejecutar
docker-compose up --build

# Ejecutar en segundo plano
docker-compose up -d --build

# Ver logs
docker-compose logs -f

# Detener
docker-compose down
```

### Opción 2: Usando Docker directamente

```bash
# Construir la imagen
docker build -t rcas-backend:latest .

# Ejecutar el contenedor
docker run -d \
  --name rcas-backend \
  -p 8081:8081 \
  -e SPRING_DATA_MONGODB_URI="mongodb+srv://edgarangelja_db_user:M3TEIlEqdGh2eT09@cluster0.iwfms3i.mongodb.net/?appName=Cluster0" \
  -e SPRING_DATA_MONGODB_DATABASE=app \
  rcas-backend:latest

# Ver logs
docker logs -f rcas-backend

# Detener
docker stop rcas-backend
docker rm rcas-backend
```

## ☁️ Despliegue en la Nube

### AWS (Elastic Container Service - ECS)

1. **Crear repositorio en ECR:**
```bash
aws ecr create-repository --repository-name rcas-backend
```

2. **Autenticarse en ECR:**
```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <tu-cuenta-id>.dkr.ecr.us-east-1.amazonaws.com
```

3. **Etiquetar y subir imagen:**
```bash
docker tag rcas-backend:latest <tu-cuenta-id>.dkr.ecr.us-east-1.amazonaws.com/rcas-backend:latest
docker push <tu-cuenta-id>.dkr.ecr.us-east-1.amazonaws.com/rcas-backend:latest
```

4. **Crear tarea y servicio en ECS** usando la consola de AWS o AWS CLI

### Google Cloud (Cloud Run)

1. **Autenticarse:**
```bash
gcloud auth login
gcloud config set project <tu-proyecto-id>
```

2. **Construir y subir:**
```bash
gcloud builds submit --tag gcr.io/<tu-proyecto-id>/rcas-backend
```

3. **Desplegar:**
```bash
gcloud run deploy rcas-backend \
  --image gcr.io/<tu-proyecto-id>/rcas-backend \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --port 8081 \
  --set-env-vars "SPRING_DATA_MONGODB_URI=mongodb+srv://...,SPRING_DATA_MONGODB_DATABASE=app"
```

### Azure (Container Instances)

1. **Crear grupo de recursos:**
```bash
az group create --name rcas-rg --location eastus
```

2. **Crear registro de contenedores:**
```bash
az acr create --resource-group rcas-rg --name rcasregistry --sku Basic
```

3. **Subir imagen:**
```bash
az acr login --name rcasregistry
docker tag rcas-backend:latest rcasregistry.azurecr.io/rcas-backend:latest
docker push rcasregistry.azurecr.io/rcas-backend:latest
```

4. **Desplegar:**
```bash
az container create \
  --resource-group rcas-rg \
  --name rcas-backend \
  --image rcasregistry.azurecr.io/rcas-backend:latest \
  --dns-name-label rcas-backend \
  --ports 8081
```

### Heroku

1. **Login y crear app:**
```bash
heroku login
heroku create rcas-backend
```

2. **Desplegar con Container Registry:**
```bash
heroku container:login
heroku container:push web -a rcas-backend
heroku container:release web -a rcas-backend
```

3. **Configurar variables de entorno:**
```bash
heroku config:set SPRING_DATA_MONGODB_URI="mongodb+srv://..." -a rcas-backend
heroku config:set SPRING_DATA_MONGODB_DATABASE=app -a rcas-backend
```

### DigitalOcean (App Platform)

1. Conecta tu repositorio de GitHub/GitLab
2. Selecciona "Dockerfile" como método de build
3. Configura las variables de entorno en la interfaz web
4. Despliega automáticamente

## 🔐 Variables de Entorno Importantes

Asegúrate de configurar estas variables en producción:

```bash
# Servidor
SERVER_PORT=8081
SERVER_SERVLET_CONTEXT_PATH=/api

# MongoDB
SPRING_DATA_MONGODB_URI=<tu-conexión-mongodb>
SPRING_DATA_MONGODB_DATABASE=app

# CORS - ¡ACTUALIZA CON TUS DOMINIOS DE PRODUCCIÓN!
CORS_ALLOWED_ORIGINS=https://tu-dominio.com,https://www.tu-dominio.com

# JWT - ¡CAMBIA EL SECRET EN PRODUCCIÓN!
JWT_SECRET=<genera-un-secret-seguro-aquí>
JWT_EXPIRATION=86400000

# Logging (usa INFO o WARN en producción)
LOGGING_LEVEL_COM=INFO
LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_DATA_MONGODB=WARN
LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_SECURITY=WARN

# Java
JAVA_OPTS=-Xmx512m -Xms256m
```

## 🔒 Seguridad en Producción

> [!WARNING]
> Antes de desplegar en producción:

1. **Cambia el JWT_SECRET** - Genera uno nuevo y seguro:
   ```bash
   openssl rand -base64 64
   ```

2. **Actualiza CORS_ALLOWED_ORIGINS** - Solo permite tus dominios de producción

3. **Usa HTTPS** - Configura certificados SSL/TLS

4. **Variables de entorno** - Nunca incluyas credenciales en el código o Dockerfile

5. **Actualiza las credenciales de MongoDB** - Considera usar secretos administrados por el proveedor cloud

## 📊 Verificación

Una vez desplegado, verifica que funcione:

```bash
# Health check
curl http://tu-dominio:8081/api/health

# Probar endpoint de autenticación
curl -X POST http://tu-dominio:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test123"}'
```

## 🛠️ Troubleshooting

### El contenedor no inicia
```bash
# Ver logs detallados
docker logs rcas-backend

# Verificar que el puerto esté disponible
netstat -an | grep 8081
```

### Problemas de conexión a MongoDB
- Verifica que la IP de tu servidor cloud esté en la lista blanca de MongoDB Atlas
- Confirma que la cadena de conexión sea correcta
- Revisa los logs de MongoDB en Atlas

### Problemas de memoria
```bash
# Aumenta la memoria disponible
docker run -e JAVA_OPTS="-Xmx1024m -Xms512m" ...
```

## 📝 Notas Adicionales

- El Dockerfile usa **multi-stage build** para optimizar el tamaño de la imagen
- La imagen final es ~200MB (Alpine Linux + JRE 17)
- Se ejecuta con un usuario no-root por seguridad
- Las dependencias de Maven se cachean para builds más rápidos
