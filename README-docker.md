# Docker Hub Push Instructions

1) Create a Docker Hub access token (recommended) or be ready with your Docker Hub username and password.

2) Copy the example env and fill values:

```bash
cp .env.docker.example .env.docker
# edit .env.docker and set DOCKERHUB_USERNAME and DOCKERHUB_TOKEN
```

3) Make the script executable (one-time):

```bash
chmod +x scripts/push-to-dockerhub.sh
```

4) Build and push the image:

```bash
./scripts/push-to-dockerhub.sh
```

Manual alternative (no script):

```bash
# build
docker build -t youruser/task-manager:latest .
# login (use token or password)
echo "YOUR_TOKEN" | docker login --username "youruser" --password-stdin
# push
docker push youruser/task-manager:latest
```

Notes:
- The project `Dockerfile` is a multi-stage Maven build that produces a runnable `app.jar` and runs on Java 17.
- Ensure your Docker host can reach any remote database you configure in `application.properties`.
