import * as THREE from 'three';
import { ElementRef, Injectable, NgZone, OnDestroy } from '@angular/core';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import { Star } from '../../model/star';
import { Planet } from '../../model/planet';

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
  private planets: { [name: string]: THREE.Mesh } = {};
  private objs: { [name: string]: THREE.Object3D } = {};

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

    const sunTexture = new THREE.TextureLoader().load('assets/img/sun/sun.jpg');
    const sunGeo = new THREE.SphereGeometry(16, 32, 32);
    const sunMat = new THREE.MeshBasicMaterial({ map: sunTexture });
    this.sun = new THREE.Mesh(sunGeo, sunMat);
    this.scene.add(this.sun);

    const pointLight = new THREE.PointLight(0xFFFFFF, 3000, 300);
    this.scene.add(pointLight);
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
    //con un for hacemos rotar a los objetos y a los planetas
    for (let planet in this.planets) {
      this.planets[planet].rotateY(0.001);
      this.objs[planet].rotateY(0.0008);
    }

    this.renderer.render(this.scene, this.camera);
  }

  public resize(): void {
    const width = window.innerWidth;
    const height = window.innerHeight;

    this.camera.aspect = width / height;
    this.camera.updateProjectionMatrix();

    this.renderer.setSize(width, height);
  }

  addPlanets(starPlanets: Planet[]) {
    starPlanets.forEach(planet => {
      const textures = ['assets/img/planets/ceresmap.jpg', 'assets/img/planets/coruscantmap.png', 'assets/img/planets/csillamap.png', 'assets/img/planets/earthmap.jpg', 'assets/img/planets/erismap.jpg', 'assets/img/planets/exoticmap.png', 'assets/img/planets/fictionalmap.png', 'assets/img/planets/haumeamap.jpg', 'assets/img/planets/jupitermap.jpg', 'assets/img/planets/korribanmap.png', 'assets/img/planets/makemakemap.jpg','assets/img/planets/marsmap.jpg', 'assets/img/planets/mercurymap.jpg', 'assets/img/planets/narshaddaamap.png','assets/img/planets/neptunemap.jpg', 'assets/img/planets/saturnmap.jpg', 'assets/img/planets/tarismap.png', 'assets/img/planets/venusmap.jpg'];
      const planetGeo = new THREE.SphereGeometry(planet.size, 30, 30);
      const planetTexture = new THREE.TextureLoader().load(textures[planet.texture]);
      const planetMat = new THREE.MeshStandardMaterial({ map: planetTexture });
      const planetMesh = new THREE.Mesh(planetGeo, planetMat);
      const obj = new THREE.Object3D();
      obj.add(planetMesh);
      if (planet.ring) {
        const planetRingGeo = new THREE.RingGeometry(planet.size + 1, planet.size + 2, 30);
        const planetRingTexture = new THREE.TextureLoader().load('assets/img/rings/uranusring.png');
        const planetRingMat = new THREE.MeshBasicMaterial({ map: planetRingTexture, side: THREE.DoubleSide });
        const ringMesh = new THREE.Mesh(planetRingGeo, planetRingMat);
        obj.add(ringMesh);
        ringMesh.position.x = planet.position;
        ringMesh.rotation.y = -0.5 * Math.PI;
      }
      this.scene.add(obj);
      planetMesh.position.x = planet.position;
      this.planets[planet.name] = planetMesh;
      this.objs[planet.name] = obj;
  }
);
  }
}
