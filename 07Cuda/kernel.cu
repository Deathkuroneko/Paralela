__global__
void sumaKernel(float* a, float* b, float* c, int n) {
    int index = blockIdx.x * blockDim.x + threadIdx.x;
    if (index < n) {
        c[index] = a[index] + b[index];
    }
}

void sumaVectores(float* a, float* b, float* c, int n){
    int threadsPerBlock = 1024;
    int numBlocks = std::ceil(n*1.0/threadsPerBlock);

    sumaKernel<<<numBlocks, threadsPerBlock>>>(a, b, c, n);
}