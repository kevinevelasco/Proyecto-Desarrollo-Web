export default `
    varying vec2 vUv;
    uniform sampler2D uSpaceTexture;
    void main() {
        gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);
        vUv = uv;
    }
`;