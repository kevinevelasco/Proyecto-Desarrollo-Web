import * as THREE from 'three';
import { ElementRef, Injectable, NgZone, OnDestroy } from '@angular/core';
import { FBXLoader } from 'three/examples/jsm/loaders/FBXLoader';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';



@Injectable({
  providedIn: 'root'
})
export class BackgroundService {

  private canvas: HTMLCanvasElement;
  private renderer: THREE.WebGLRenderer;
  private scene: THREE.Scene;
  private light: THREE.AmbientLight;
  private frameId: number = 0;
  private camera: THREE.PerspectiveCamera;

  private sun: THREE.Mesh;
  private stars: THREE.Mesh[] = []; //creamos una lista de estrellas
  private mixers: THREE.AnimationMixer[] = [];
  previousRAF: number = 0;
  model: THREE.Group<THREE.Object3DEventMap>;

  public constructor(private ngZone: NgZone) {}

  public mouseX = window.innerWidth / 2;
  public mouseY = window.innerHeight / 2;

  public controls: OrbitControls;

  public ngOnDestroy(): void {
    console.log('ngOnDestroy');
    if (this.frameId != null) {
      cancelAnimationFrame(this.frameId);
    }
    if (this.renderer != null) {
      this.renderer.dispose();
      this.renderer.forceContextLoss();
      this.renderer = null as any;
      this.stars = [];
    }
  }

  public createScene(canvas: ElementRef<HTMLCanvasElement>): void {
    // The first step is to get the reference of the canvas element from our HTML document
    this.canvas = canvas.nativeElement;

    this.renderer = new THREE.WebGLRenderer({
      canvas: this.canvas,
      alpha: true, // transparent background
      antialias: true, // smooth edges
    });
    this.renderer.setSize(window.innerWidth, window.innerHeight);

    // create the scene
    this.scene = new THREE.Scene();

    this.camera = new THREE.PerspectiveCamera(
      45,
      window.innerWidth / window.innerHeight,
      0.1,
      1000
    );

    this.camera.position.set(0, 0, 150); // Cambiamos la posición de la cámara para que mire hacia el cubo y el sol
    this.scene.add(this.camera);

    // soft white light
    this.light = new THREE.AmbientLight(0x404040);
    this.light.position.z = 10;
    this.scene.add(this.light);

    for (var z = -1000; z < 1000; z += 20) {
      // Make a sphere (exactly the same as before).
      var geometry = new THREE.SphereGeometry(0.5, 32, 32);
      var material = new THREE.MeshBasicMaterial({ color: 0xffffff });
      var sphere = new THREE.Mesh(geometry, material);

      // This time we give the sphere random x and y positions between -500 and 500
      sphere.position.x = Math.random() * 1000 - 500;
      sphere.position.y = Math.random() * 1000 - 500;

      // Then set the z position to where it is in the loop (distance of camera)
      sphere.position.z = z;

      // scale it up a bit
      sphere.scale.x = sphere.scale.y = 2;

      //add the sphere to the scene
      this.scene.add(sphere);

      //finally push it to the stars array
      this.stars.push(sphere);
    }

    // const sunTexture = new THREE.TextureLoader().load('assets/img/sun/sun.jpg');
    // const sunGeo = new THREE.SphereGeometry(16, 32, 32);
    // const sunMat = new THREE.MeshBasicMaterial({ map: sunTexture });
    // this.sun = new THREE.Mesh(sunGeo, sunMat);
    // this.scene.add(this.sun);

    const loader = new FBXLoader();
    loader.setPath('assets/3d/models/');
    loader.load('Vanguard By T. Choonyung.fbx', (fbx) => {
      fbx.scale.setScalar(0.4);
      fbx.position.set(-80, -40, 0);
      fbx.rotation.set(0, 0, 0);
      fbx.traverse(c => {
        c.castShadow = true;
        c.receiveShadow = true;
      });
      const anim = new FBXLoader();
      anim.setPath('assets/3d/animations/');
      anim.load('Praying.fbx', (anim) => {
        const m = new THREE.AnimationMixer(fbx);
        this.mixers.push(m); // Save mixer for later updates
        const idle = m.clipAction(anim.animations[0]);
        idle.play();
      });
      this.scene.add(fbx);
      this.model = fbx;
      this.controls = new OrbitControls(this.camera, this.renderer.domElement);
    })

    //agregamos iluminación
    const light = new THREE.DirectionalLight(0xffffff, 1);
    light.position.set(0, 0, 1);
    this.scene.add(light);
  }

  public animate(): void {
    // We have to run this outside angular zones,
    // because it could trigger heavy changeDetection cycles.
    this.ngZone.runOutsideAngular(() => {
      if (document.readyState !== 'loading') {
        this.render();
      } else {
        window.addEventListener('DOMContentLoaded', () => {
          this.render();
        });
      }

      window.addEventListener('resize', () => {
        this.resize();
      });
      window.addEventListener('mousemove', (event) => {
        this.mouseX = event.clientX;
        this.mouseY = event.clientY;
      });
    });
  }

  public render(): void {

    this.frameId = requestAnimationFrame((t) => {
      this.render();

      if (this.previousRAF === null) {
        this.previousRAF = t;
      }
    

    for (var i = 0; i < this.stars.length; i++) {
      const star = this.stars[i];

      // and move it forward dependent on the mouseY position.
      star.position.z += i / 10;

      // if the particle is too close move it to the back
      if (star.position.z > 1000) star.position.z -= 2000;
    }
    
    // Calculamos la orientación del modelo en función de la posición del ratón
    const mouseXNorm = (this.mouseX / window.innerWidth) ; // Normalizamos la posición del ratón en el eje X
    const mouseYNorm = (this.mouseY / window.innerHeight) ; // Normalizamos la posición del ratón en el eje Y

    const maxRotationX = Math.PI / 6; // Máxima rotación permitida en el eje X
    const maxRotationY = Math.PI / 6; // Máxima rotación permitida en el eje Y

    const targetRotationX = maxRotationX * mouseYNorm; // Calculamos la rotación en el eje X en función de la posición del ratón
    const targetRotationY = maxRotationY * mouseXNorm; // Calculamos la rotación en el eje Y en función de la posición del ratón

    // Aplicamos la rotación al modelo
    this.model.rotation.x = targetRotationX;
    this.model.rotation.y = targetRotationY;

    // Aplicamos la rotación al modelo solo en el eje X
    this.model.rotation.x = targetRotationX;
    this.renderer.render(this.scene, this.camera);
    this.step(t - this.previousRAF);
    this.previousRAF = t;
  });
  }

  public resize(): void {
    const width = window.innerWidth;
    const height = window.innerHeight;

    this.camera.aspect = width / height;
    this.camera.updateProjectionMatrix();

    this.renderer.setSize(width, height);
  }

  public step(timeElapsed: number) {
    const timeElapsedS = timeElapsed * 0.001;
    if (this.mixers) {
      this.mixers.map(m => m.update(timeElapsedS));
    }
  }
}

