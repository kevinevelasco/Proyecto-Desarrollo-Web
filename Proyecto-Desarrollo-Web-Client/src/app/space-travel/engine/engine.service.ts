import * as THREE from 'three';
import { ElementRef, Injectable, NgZone, OnDestroy } from '@angular/core';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import { Planet } from '../../model/planet';
import vertexShader from  './vertexShader';
import fragmentShader from './fragmentShader';

@Injectable({ providedIn: 'root' })
export class EngineService implements OnDestroy {

  private canvas: HTMLCanvasElement;
  private renderer: THREE.WebGLRenderer;
  private scene: THREE.Scene;
  private light: THREE.AmbientLight;
  private frameId: number = 0;
  private camera: THREE.PerspectiveCamera;

  private sun: THREE.Mesh;
  private stars: THREE.Mesh[] = []; //creamos una lista de estrellas

  //creamos una lista de planetas que tiene la estrella
  private planets: { [name: string]: THREE.Mesh } = {};
  private objs: { [name: string]: THREE.Object3D } = {};
  private infoPlanet: boolean = false;
  private info: boolean = false;
  private currentPlanet: Planet;

  public constructor(private ngZone: NgZone) {}

  public ngOnDestroy(): void {
    console.log("ngOnDestroy");
    if (this.frameId != null) {
      cancelAnimationFrame(this.frameId);
    }
    if (this.renderer != null) {
      this.renderer.dispose();
      this.renderer.forceContextLoss();
    }

    document.body.removeChild(this.renderer.domElement);
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

    this.camera.position.set(0, 0, 150); // Cambiamos la posición de la cámara para que mire hacia el cubo y el sol
    this.scene.add(this.camera);

    // soft white light
    this.light = new THREE.AmbientLight(0x404040);
    this.light.position.z = 10;
    this.scene.add(this.light);


    for ( var z= -1000; z < 1000; z+=20 ) {
		
      // Make a sphere (exactly the same as before). 
      var geometry   = new THREE.SphereGeometry(0.5, 32, 32)
      var material = new THREE.MeshBasicMaterial( {color: 0xffffff} );
      var sphere = new THREE.Mesh(geometry, material)

      // This time we give the sphere random x and y positions between -500 and 500
      sphere.position.x = Math.random() * 1000 - 500;
      sphere.position.y = Math.random() * 1000 - 500;

      // Then set the z position to where it is in the loop (distance of camera)
      sphere.position.z = z;

      // scale it up a bit
      sphere.scale.x = sphere.scale.y = 2;

      //add the sphere to the scene
      this.scene.add( sphere );

      //finally push it to the stars array 
      this.stars.push(sphere); 
    }

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

    for(var i=0; i<this.stars.length; i++) {
			
			const star = this.stars[i]; 
				
			// and move it forward dependent on the mouseY position. 
			star.position.z +=  i/10;
				
			// if the particle is too close move it to the back
			if(star.position.z>1000) star.position.z-=2000; 
			
		}
    this.sun.rotateY(0.004);
    //con un for hacemos rotar a los objetos y a los planetas
    for (let planet in this.planets) {
      if(!this.info){
        this.planets[planet].rotateY(0.001);
        this.objs[planet].rotateY(0.0008);
      } else {
        if (planet == this.currentPlanet.name && this.infoPlanet) {
          var zoomIn = this.currentPlanet.size + 10;
          if(this.camera.position.z > this.objs[planet].position.z + zoomIn){
            this.camera.position.z -= 0.1;
          } 
        }
    }

    this.renderer.render(this.scene, this.camera);
  }
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

  showPlanet(planet: Planet) {
    this.camera.position.set(0, 0, 150);
    this.infoPlanet = true;
    this.info=true;
    this.currentPlanet = planet;
    this.camera.position.set(planet.position, 0, 50);

  }

  setInfo(info: boolean) {
    this.info = info;
    this.camera.position.set(0, 0, 150);
  }
}
