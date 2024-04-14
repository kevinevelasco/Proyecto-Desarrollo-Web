import * as THREE from 'three';
import { ElementRef, Injectable, NgZone, OnDestroy } from '@angular/core';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import { Planet } from '../../model/planet';
import { InteractionManager } from 'three.interactive';
import { Subject } from 'rxjs';


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

  public constructor(private ngZone: NgZone) {}

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

    const sunTexture = new THREE.TextureLoader().load('assets/img/sun/sun.jpg');
    const sunGeo = new THREE.SphereGeometry(16, 32, 32);
    const sunMat = new THREE.MeshBasicMaterial({ map: sunTexture });
    this.sun = new THREE.Mesh(sunGeo, sunMat);
    this.scene.add(this.sun);

    const pointLight = new THREE.PointLight(0xffffff, 3000, 300);
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

    for (var i = 0; i < this.stars.length; i++) {
      const star = this.stars[i];

      // and move it forward dependent on the mouseY position.
      star.position.z += i / 10;

      // if the particle is too close move it to the back
      if (star.position.z > 1000) star.position.z -= 2000;
    }
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
}
