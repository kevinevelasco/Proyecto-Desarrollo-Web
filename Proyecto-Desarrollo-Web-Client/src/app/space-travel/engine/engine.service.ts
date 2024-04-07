import * as THREE from 'three';
import { ElementRef, Injectable, NgZone, OnDestroy } from '@angular/core';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import { Star } from '../../model/star';

@Injectable({ providedIn: 'root' })
export class EngineService implements OnDestroy {
  private canvas: HTMLCanvasElement;
  private renderer: THREE.WebGLRenderer;
  private scene: THREE.Scene;
  private light: THREE.AmbientLight;
  private frameId: number = 0;
  private camera: THREE.PerspectiveCamera;

  private sun: THREE.Mesh;

  //creamos una lista de planetas que tiene la estrella
  private planets: THREE.Mesh[] = [];

  public constructor(private ngZone: NgZone) {}

  public ngOnDestroy(): void {
    if (this.frameId != null) {
      cancelAnimationFrame(this.frameId);
    }
    if (this.renderer != null) {
      this.renderer.dispose();
      this.renderer.forceContextLoss();
    }
  }

  public createScene(canvas: ElementRef<HTMLCanvasElement>): void {
    // The first step is to get the reference of the canvas element from our HTML document
    this.canvas = canvas.nativeElement;

    this.renderer = new THREE.WebGLRenderer({
      canvas: this.canvas,
      alpha: true, // transparent background
      antialias: true // smooth edges
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

    const orbit = new OrbitControls(this.camera, this.renderer.domElement);
    this.camera.position.set(0, 0, 50); // Cambiamos la posición de la cámara para que mire hacia el cubo y el sol
    orbit.update();
    this.scene.add(this.camera);

    // soft white light
    this.light = new THREE.AmbientLight(0x404040);
    this.light.position.z = 10;
    this.scene.add(this.light);

    const sunTexture = new THREE.TextureLoader().load('assets/img/sun.jpg');
    const sunGeo = new THREE.SphereGeometry(16, 32, 32);
    const sunMat = new THREE.MeshBasicMaterial({ map: sunTexture });
    this.sun = new THREE.Mesh(sunGeo, sunMat);
    this.scene.add(this.sun);
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
    });
  }

  public render(): void {
    this.frameId = requestAnimationFrame(() => {
      this.render();
    });

    this.sun.rotateY(0.004);
    this.renderer.render(this.scene, this.camera);
  }

  public resize(): void {
    const width = window.innerWidth;
    const height = window.innerHeight;

    this.camera.aspect = width / height;
    this.camera.updateProjectionMatrix();

    this.renderer.setSize(width, height);
  }

  //para saber los datos de la estrella en la que estoy, dependo de spacecraft
  //ya que es la nave de la cual hace parte el jugador que se logueó
  public getStarData() {
    
  }
}
