set -e
! podman manifest exists ssx-placeholderserver || podman manifest rm ssx-placeholderserver
podman manifest create ssx-placeholderserver
podman build --platform=linux/arm64,linux/amd64 --manifest ssx-placeholderserver .
podman manifest push --all ssx-placeholderserver docker://docker.io/derkades/ssx-placeholderserver
