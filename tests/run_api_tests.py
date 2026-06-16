#!/usr/bin/env python3
import requests
import json
import time
from datetime import datetime, timedelta

BASE = "http://localhost:10000/api/v1"
REPORT_PATH = "reports/api_test_report.json"

session = requests.Session()
report = {"summary":{}, "results": []}

creds = {"username": "testuser", "password": "Test1234", "roles": ["ROLE_USER"]}

def save_report():
    import os
    os.makedirs('reports', exist_ok=True)
    with open(REPORT_PATH, 'w') as f:
        json.dump(report, f, indent=2, default=str)
    print(f"Report saved to {REPORT_PATH}")

# 1) Sign-up (if user exists, continue)
try:
    r = session.post(f"{BASE}/authentication/sign-up", json=creds, timeout=10)
    report['results'].append({"endpoint":"/authentication/sign-up","method":"POST","status_code":r.status_code,"body":r.text})
    print('/sign-up', r.status_code)
except Exception as e:
    report['results'].append({"endpoint":"/authentication/sign-up","method":"POST","error":str(e)})
    print('sign-up error', e)

# 2) Sign-in
token = None
try:
    r = session.post(f"{BASE}/authentication/sign-in", json={"username":creds['username'],"password":creds['password']}, timeout=10)
    report['results'].append({"endpoint":"/authentication/sign-in","method":"POST","status_code":r.status_code,"body":r.text})
    print('/sign-in', r.status_code)
    if r.status_code == 200:
        j = r.json()
        token = j.get('token') or j.get('accessToken') or j.get('jwt')
        report['summary']['token_received'] = bool(token)
        if token:
            session.headers.update({'Authorization': f'Bearer {token}'})
except Exception as e:
    report['results'].append({"endpoint":"/authentication/sign-in","method":"POST","error":str(e)})
    print('sign-in error', e)

# Helper to run request and record
def run_request(method, path, json_payload=None):
    url = BASE + path
    try:
        r = session.request(method, url, json=json_payload, timeout=15)
        entry = {"endpoint": path, "method": method, "status_code": r.status_code}
        try:
            entry['response_json'] = r.json()
        except Exception:
            entry['response_text'] = r.text
        report['results'].append(entry)
        print(method, path, r.status_code)
        return r
    except Exception as e:
        report['results'].append({"endpoint": path, "method": method, "error": str(e)})
        print('request error', method, path, e)
        return None

# Test public endpoint: roles
run_request('GET', '/roles')

# Prepare resources: coaches, courts, user-profiles
coach_payload = {"name":"Test Coach","expertise":"Tennis","phone":"+511234567"}
court_payload = {"name":"Court 1","location":"Local Gym","type":"INDOOR"}
user_profile_payload = {"name":"Test User","email":"test@example.com","phone":"+511234567"}

coach = run_request('POST', '/coaches', coach_payload)
coach_id = None
if coach and coach.status_code in (200,201):
    try:
        coach_id = coach.json().get('id')
    except Exception:
        pass

court = run_request('POST', '/courts', court_payload)
court_id = None
if court and court.status_code in (200,201):
    try:
        court_id = court.json().get('id')
    except Exception:
        pass

# Create user-profile
profile = run_request('POST', '/user-profiles', user_profile_payload)
profile_id = None
if profile and profile.status_code in (200,201):
    try:
        profile_id = profile.json().get('id')
    except Exception:
        pass

# If sign-up returned user id via earlier result, find it; otherwise query /users to find testuser
user_id = None
for res in report['results']:
    if res.get('endpoint') == '/authentication/sign-up' and res.get('status_code') in (200,201):
        try:
            body = json.loads(res.get('body') or '{}')
            user_id = body.get('id')
        except Exception:
            pass

if not user_id:
    r = run_request('GET', '/users')
    if r and r.status_code == 200:
        try:
            users = r.json()
            for u in users:
                if u.get('username') == creds['username']:
                    user_id = u.get('id')
                    break
        except Exception:
            pass

# Create booking if we have user_id and court_id
booking_id = None
if user_id and court_id:
    start = (datetime.utcnow() + timedelta(hours=1)).isoformat()
    end = (datetime.utcnow() + timedelta(hours=2)).isoformat()
    booking_payload = {"startTime": start, "endTime": end, "userId": user_id, "courtId": court_id}
    b = run_request('POST', '/bookings', booking_payload)
    if b and b.status_code in (200,201):
        try:
            booking_id = b.json().get('id')
        except Exception:
            pass

# Create payment if user_id
payment_id = None
if user_id:
    payment_payload = {"amount": 50.0, "userId": user_id}
    p = run_request('POST', '/payments', payment_payload)
    if p and p.status_code in (200,201):
        try:
            payment_id = p.json().get('id')
        except Exception:
            pass

# Test GET list endpoints
run_request('GET', '/coaches')
run_request('GET', '/courts')
run_request('GET', '/user-profiles')
run_request('GET', '/bookings')
run_request('GET', '/payments')
run_request('GET', '/users')

# If created resources, test GET by id, PUT and DELETE
if coach_id:
    run_request('GET', f'/coaches/{coach_id}')
    run_request('PUT', f'/coaches/{coach_id}', {"name":"Updated Coach","expertise":"Padel","phone":"+511000000"})
    run_request('DELETE', f'/coaches/{coach_id}')

if court_id:
    run_request('GET', f'/courts/{court_id}')
    run_request('PUT', f'/courts/{court_id}', {"name":"Updated Court","location":"New Place","type":"OUTDOOR"})
    run_request('DELETE', f'/courts/{court_id}')

if profile_id:
    run_request('GET', f'/user-profiles/{profile_id}')
    run_request('PUT', f'/user-profiles/{profile_id}', {"name":"Updated Name","email":"updated@example.com","phone":"+519999999"})
    run_request('DELETE', f'/user-profiles/{profile_id}')

if booking_id:
    run_request('GET', f'/bookings/{booking_id}')
    run_request('PUT', f'/bookings/{booking_id}', {"startTime": start, "endTime": end})
    run_request('DELETE', f'/bookings/{booking_id}')

if payment_id:
    run_request('GET', f'/payments/{payment_id}')

# Report final status summary
report['summary']['created_ids'] = {"coach_id": coach_id, "court_id": court_id, "profile_id": profile_id, "user_id": user_id, "booking_id": booking_id, "payment_id": payment_id}
save_report()
print('Done')
